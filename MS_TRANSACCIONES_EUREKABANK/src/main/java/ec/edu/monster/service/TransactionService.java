package ec.edu.monster.service;

import ec.edu.monster.dto.AccountInfoDTO;
import ec.edu.monster.dto.DepositDTO;
import ec.edu.monster.dto.TransactionResponseDTO;
import ec.edu.monster.dto.TransferDTO;
import ec.edu.monster.dto.WithdrawDTO;
import ec.edu.monster.exception.TransactionException;
import ec.edu.monster.model.Transaction;
import ec.edu.monster.model.TransactionType;
import ec.edu.monster.model.TransferType;
import ec.edu.monster.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TransactionService para MS_TRANSACCIONES.
 *
 * Diferencias clave vs. el monolito:
 *  - No hay @Inject AccountService ni @Inject ParameterService.
 *  - La verificación/obtención de cuentas activas se hace llamando a MS_CUENTAS via HTTP:
 *      GET  {MS_CUENTAS_URL}/accounts/{id}
 *  - La actualización de saldos se hace llamando a MS_CUENTAS via HTTP:
 *      PUT  {MS_CUENTAS_URL}/accounts/{id}/balance   Body: {"delta": <amount>}
 *  - Los parámetros del sistema (tasas, límites) se obtienen de MS_CLIENTES via HTTP:
 *      GET  {MS_CLIENTES_URL}/parameters/{key}
 */
@ApplicationScoped
public class TransactionService {

    // ── Parámetros del sistema (keys) ─────────────────────────────────────────
    private static final String PARAM_TRANSFER_FEE         = "transfer.fee.percentage";
    private static final String PARAM_TRANSFER_DAILY_LIMIT = "transfer.daily.limit";
    private static final String PARAM_WITHDRAW_DAILY_LIMIT = "withdraw.daily.limit";
    private static final String PARAM_MIN_BALANCE          = "account.min.balance";
    private static final String PARAM_CREDIT_FEE           = "transfer.credit.fee.percentage";
    private static final String PARAM_DEBIT_FEE            = "transfer.debit.fee.percentage";
    private static final String PARAM_CREDIT_DAILY_LIMIT   = "transfer.credit.daily.limit";
    private static final String PARAM_DEBIT_DAILY_LIMIT    = "transfer.debit.daily.limit";

    // ── URLs de otros microservicios ──────────────────────────────────────────
    private static final String MS_CUENTAS_URL =
            System.getenv().getOrDefault("MS_CUENTAS_URL", "http://localhost:8080/MS_CUENTAS_EUREKABANK/api");
    private static final String MS_CLIENTES_URL =
            System.getenv().getOrDefault("MS_CLIENTES_URL", "http://localhost:8080/MS_CLIENTES_EUREKABANK/api");

    @Inject
    private TransactionRepository transactionRepository;

    // ── DEPOSIT ───────────────────────────────────────────────────────────────

    @Transactional
    public TransactionResponseDTO deposit(DepositDTO request) {
        if (request == null)               throw new TransactionException("Request body is required", 400);
        if (request.getAccountId() == null) throw new TransactionException("Account id is required", 400);
        validateAmount(request.getAmount());

        // Verificar que la cuenta existe y está activa
        AccountInfoDTO account = requireActiveAccountHttp(request.getAccountId());

        // Actualizar saldo en MS_CUENTAS
        applyBalanceDeltaHttp(account.getId(), request.getAmount());

        Transaction transaction = buildTransaction(
                TransactionType.DEPOSIT, null,
                request.getAmount(), BigDecimal.ZERO,
                account.getId(), null, request.getDescription()
        );
        transactionRepository.create(transaction);
        return toResponse(transaction);
    }

    // ── WITHDRAW ──────────────────────────────────────────────────────────────

    @Transactional
    public TransactionResponseDTO withdraw(WithdrawDTO request) {
        if (request == null)               throw new TransactionException("Request body is required", 400);
        if (request.getAccountId() == null) throw new TransactionException("Account id is required", 400);
        validateAmount(request.getAmount());

        AccountInfoDTO account = requireActiveAccountHttp(request.getAccountId());

        // Límite diario de retiro
        BigDecimal withdrawDailyLimit = parseParam(
                getParameterValueOrDefault(PARAM_WITHDRAW_DAILY_LIMIT, "0"), PARAM_WITHDRAW_DAILY_LIMIT);
        if (withdrawDailyLimit.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal todayWithdrawn = getDailyAmount(request.getAccountId(), TransactionType.WITHDRAW);
            if (todayWithdrawn.add(request.getAmount()).compareTo(withdrawDailyLimit) > 0) {
                throw new TransactionException(
                        "Daily withdraw limit exceeded. Limit: " + withdrawDailyLimit
                                + ", already withdrawn today: " + todayWithdrawn, 400);
            }
        }

        // Saldo mínimo
        BigDecimal minBalance = parseParam(
                getParameterValueOrDefault(PARAM_MIN_BALANCE, "0"), PARAM_MIN_BALANCE);
        BigDecimal balanceAfter = account.getBalance().subtract(request.getAmount());
        if (balanceAfter.compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionException("Insufficient balance", 400);
        }
        if (balanceAfter.compareTo(minBalance) < 0) {
            throw new TransactionException(
                    "Withdrawal would leave balance below the required minimum of " + minBalance, 400);
        }

        // Aplicar débito en MS_CUENTAS
        applyBalanceDeltaHttp(account.getId(), request.getAmount().negate());

        Transaction transaction = buildTransaction(
                TransactionType.WITHDRAW, null,
                request.getAmount(), BigDecimal.ZERO,
                account.getId(), null, request.getDescription()
        );
        transactionRepository.create(transaction);
        return toResponse(transaction);
    }

    // ── TRANSFER ──────────────────────────────────────────────────────────────

    @Transactional
    public TransactionResponseDTO transfer(TransferDTO request) {
        if (request == null) throw new TransactionException("Request body is required", 400);
        if (request.getSourceAccountId() == null || request.getTargetAccountId() == null) {
            throw new TransactionException("Source and target account are required", 400);
        }
        if (request.getSourceAccountId().equals(request.getTargetAccountId())) {
            throw new TransactionException("Source and target accounts must be different", 400);
        }
        if (request.getTransferType() == null) {
            throw new TransactionException("Transfer type is required (CREDIT or DEBIT)", 400);
        }
        validateAmount(request.getAmount());

        TransferType transferType = request.getTransferType();
        AccountInfoDTO source = requireActiveAccountHttp(request.getSourceAccountId());
        AccountInfoDTO target = requireActiveAccountHttp(request.getTargetAccountId());

        // Calcular comisión
        String feeParamKey      = transferType == TransferType.CREDIT ? PARAM_CREDIT_FEE : PARAM_DEBIT_FEE;
        String genericFeeDefault = getParameterValueOrDefault(PARAM_TRANSFER_FEE, "0");
        BigDecimal feePercentage = parseParam(getParameterValueOrDefault(feeParamKey, genericFeeDefault), feeParamKey);
        BigDecimal fee           = request.getAmount().multiply(feePercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalDeducted = request.getAmount().add(fee);

        // Límite diario de transferencia
        String limitParamKey       = transferType == TransferType.CREDIT ? PARAM_CREDIT_DAILY_LIMIT : PARAM_DEBIT_DAILY_LIMIT;
        String genericLimitDefault  = getParameterValueOrDefault(PARAM_TRANSFER_DAILY_LIMIT, "0");
        BigDecimal dailyLimit       = parseParam(getParameterValueOrDefault(limitParamKey, genericLimitDefault), limitParamKey);
        if (dailyLimit.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal todayTransferred = getDailyAmount(request.getSourceAccountId(), TransactionType.TRANSFER);
            if (todayTransferred.add(request.getAmount()).compareTo(dailyLimit) > 0) {
                throw new TransactionException(
                        "Daily " + transferType.name().toLowerCase() + " transfer limit exceeded. Limit: "
                                + dailyLimit + ", already transferred today: " + todayTransferred, 400);
            }
        }

        // Verificar saldo suficiente
        if (source.getBalance().compareTo(totalDeducted) < 0) {
            String msg = fee.compareTo(BigDecimal.ZERO) > 0
                    ? "Insufficient balance. Transfer requires " + totalDeducted
                            + " (amount: " + request.getAmount() + " + fee: " + fee + ")"
                    : "Insufficient balance";
            throw new TransactionException(msg, 400);
        }

        // Aplicar balances en MS_CUENTAS (débito origen, crédito destino)
        applyBalanceDeltaHttp(source.getId(), totalDeducted.negate());
        applyBalanceDeltaHttp(target.getId(), request.getAmount());

        Transaction transaction = buildTransaction(
                TransactionType.TRANSFER, transferType,
                request.getAmount(), fee,
                source.getId(), target.getId(), request.getDescription()
        );
        transactionRepository.create(transaction);
        return toResponse(transaction);
    }

    // ── QUERY ─────────────────────────────────────────────────────────────────

    public List<TransactionResponseDTO> getTransactionsByAccount(Long accountId) {
        if (accountId == null) throw new TransactionException("Account id is required", 400);
        // Verificar que la cuenta existe
        requireActiveAccountHttp(accountId);
        return transactionRepository.findByAccountId(accountId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    // ── Llamadas HTTP a MS_CUENTAS ────────────────────────────────────────────

    /**
     * GET {MS_CUENTAS_URL}/accounts/{id}
     * Verifica que la cuenta existe y está ACTIVA. Retorna AccountInfoDTO con balance actual.
     */
    private AccountInfoDTO requireActiveAccountHttp(Long accountId) {
        Client httpClient = ClientBuilder.newClient();
        try {
            Response response = httpClient
                    .target(MS_CUENTAS_URL)
                    .path("/accounts/" + accountId)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 404) throw new TransactionException("Account not found: " + accountId, 404);
            if (response.getStatus() >= 400) throw new TransactionException("Error fetching account (HTTP " + response.getStatus() + ")", 502);

            String json = response.readEntity(String.class);
            return parseAccountInfo(json);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new TransactionException("MS_CUENTAS is unreachable: " + e.getMessage(), 503);
        } finally {
            httpClient.close();
        }
    }

    /**
     * PUT {MS_CUENTAS_URL}/accounts/{id}/balance
     * Aplica un delta al saldo (positivo=crédito, negativo=débito).
     */
    private void applyBalanceDeltaHttp(Long accountId, BigDecimal delta) {
        Client httpClient = ClientBuilder.newClient();
        try {
            String body = "{\"delta\":" + delta.toPlainString() + "}";
            Response response = httpClient
                    .target(MS_CUENTAS_URL)
                    .path("/accounts/" + accountId + "/balance")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(body));

            if (response.getStatus() >= 400) {
                throw new TransactionException(
                        "Error updating balance for account " + accountId + " (HTTP " + response.getStatus() + ")", 502);
            }
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new TransactionException("MS_CUENTAS is unreachable during balance update: " + e.getMessage(), 503);
        } finally {
            httpClient.close();
        }
    }

    // ── Llamadas HTTP a MS_CLIENTES (parámetros) ──────────────────────────────

    /**
     * GET {MS_CLIENTES_URL}/parameters/{key}
     * Retorna el valor del parámetro, o defaultValue si no existe (HTTP 404).
     */
    private String getParameterValueOrDefault(String key, String defaultValue) {
        Client httpClient = ClientBuilder.newClient();
        try {
            Response response = httpClient
                    .target(MS_CLIENTES_URL)
                    .path("/parameters/" + key)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 404) return defaultValue;
            if (response.getStatus() >= 400) return defaultValue;

            String json = response.readEntity(String.class);
            try (JsonReader reader = Json.createReader(new StringReader(json))) {
                JsonObject obj = reader.readObject();
                return obj.getString("value", defaultValue);
            }
        } catch (Exception e) {
            return defaultValue; // Fallo silencioso — usa default
        } finally {
            httpClient.close();
        }
    }

    // ── Helpers privados ─────────────────────────────────────────────────────

    private AccountInfoDTO parseAccountInfo(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject obj = reader.readObject();
            Long id = obj.getJsonNumber("id").longValueExact();
            BigDecimal balance = new BigDecimal(obj.getJsonNumber("balance").toString());
            String status = obj.getString("status", "");
            if (!"ACTIVE".equals(status)) {
                throw new TransactionException("Account " + id + " is not active", 400);
            }
            return new AccountInfoDTO(id, balance);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new TransactionException("Error parsing account response: " + e.getMessage(), 500);
        }
    }

    private Transaction buildTransaction(TransactionType type, TransferType transferType,
            BigDecimal amount, BigDecimal fee,
            Long sourceAccountId, Long targetAccountId, String description) {
        Transaction t = new Transaction();
        t.setType(type);
        t.setTransferType(transferType);
        t.setAmount(amount);
        t.setFee(fee);
        t.setDate(LocalDateTime.now());
        t.setSourceAccountId(sourceAccountId);
        t.setTargetAccountId(targetAccountId);
        t.setDescription(normalize(description));
        return t;
    }

    private TransactionResponseDTO toResponse(Transaction t) {
        return new TransactionResponseDTO(
                t.getId(), t.getType(), t.getTransferType(),
                t.getAmount(), t.getFee(), t.getDate(),
                t.getSourceAccountId(), t.getTargetAccountId(),
                t.getDescription()
        );
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Amount must be greater than zero", 400);
        }
    }

    private BigDecimal getDailyAmount(Long accountId, TransactionType type) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay   = startOfDay.plusDays(1);
        return transactionRepository.sumDailyAmount(accountId, type, startOfDay, endOfDay);
    }

    private BigDecimal parseParam(String value, String key) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new TransactionException("Invalid system parameter value for: " + key, 500);
        }
    }

    private String normalize(String value) { return value == null ? null : value.trim(); }
}
