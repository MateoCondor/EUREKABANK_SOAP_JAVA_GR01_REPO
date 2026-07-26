package ec.edu.monster.service;

import ec.edu.monster.dto.AccountRequestDTO;
import ec.edu.monster.dto.AccountResponseDTO;
import ec.edu.monster.dto.BalanceDeltaDTO;
import ec.edu.monster.exception.AccountException;
import ec.edu.monster.model.Account;
import ec.edu.monster.model.AccountStatus;
import ec.edu.monster.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AccountService para MS_CUENTAS.
 *
 * Diferencia clave vs. el monolito:
 *   - Ya no hay @Inject ClientRepository.
 *   - La verificación de existencia del cliente se hace mediante una llamada HTTP GET
 *     a MS_CLIENTES (URL configurable via variable de entorno MS_CLIENTES_URL).
 *   - El endpoint PUT /accounts/{id}/balance permite a MS_TRANSACCIONES actualizar
 *     saldos sin acceder directamente a la BD de este microservicio.
 */
@ApplicationScoped
public class AccountService {

    private static final int ACCOUNT_NUMBER_LENGTH = 12;

    /** URL base de MS_CLIENTES. Configurable vía variable de entorno. */
    private static final String MS_CLIENTES_URL =
            System.getenv().getOrDefault("MS_CLIENTES_URL", "http://localhost:8080/MS_CLIENTES_EUREKABANK/api");

    @Inject
    private AccountRepository accountRepository;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        if (request == null)               throw new AccountException("Request body is required", 400);
        if (request.getClientId() == null) throw new AccountException("Client id is required", 400);
        if (request.getType() == null)     throw new AccountException("Account type is required", 400);

        // Verificar que el cliente existe en MS_CLIENTES via HTTP
        verifyClientExists(request.getClientId());

        Account account = new Account();
        account.setClientId(request.getClientId());
        account.setType(request.getType());
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateUniqueAccountNumber());

        accountRepository.create(account);
        return toResponse(account);
    }

    public AccountResponseDTO getAccountById(Long id) {
        if (id == null) throw new AccountException("Account id is required", 400);
        return toResponse(accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found", 404)));
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AccountResponseDTO> getAccountsByClient(Long clientId) {
        if (clientId == null) throw new AccountException("Client id is required", 400);
        verifyClientExists(clientId);
        return accountRepository.findByClientId(clientId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public AccountResponseDTO updateStatus(Long id, AccountStatus status) {
        if (id == null)     throw new AccountException("Account id is required", 400);
        if (status == null) throw new AccountException("Account status is required", 400);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found", 404));
        account.setStatus(status);
        return toResponse(accountRepository.update(account));
    }

    public BigDecimal getBalance(Long id) {
        if (id == null) throw new AccountException("Account id is required", 400);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found", 404));
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountException("Account is not active", 400);
        }
        return account.getBalance();
    }

    // ── Uso interno por MS_TRANSACCIONES ─────────────────────────────────────

    /**
     * Retorna la Account si existe y está ACTIVA.
     * Llamado desde AccountController.updateBalance() invocado por MS_TRANSACCIONES.
     */
    public Account requireActiveAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("Account not found", 404));
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountException("Account is not active", 400);
        }
        return account;
    }

    public Account requireAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("Account not found", 404));
    }

    /**
     * Aplica un delta (positivo = crédito, negativo = débito) al saldo de la cuenta.
     * Usado por MS_TRANSACCIONES via PUT /accounts/{id}/balance.
     */
    @Transactional
    public AccountResponseDTO applyBalanceDelta(Long id, BigDecimal delta) {
        if (id == null || delta == null) throw new AccountException("Account id and delta are required", 400);

        Account account = requireActiveAccount(id);
        BigDecimal newBalance = account.getBalance().add(delta);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountException("Insufficient balance", 400);
        }
        account.setBalance(newBalance);
        return toResponse(accountRepository.update(account));
    }

    // ── Helpers privados ─────────────────────────────────────────────────────

    /**
     * Llama a GET {MS_CLIENTES_URL}/clients/{clientId}.
     * Si el cliente no existe (404) o hay error de red, lanza AccountException.
     */
    private void verifyClientExists(Long clientId) {
        Client httpClient = ClientBuilder.newClient();
        try {
            Response response = httpClient
                    .target(MS_CLIENTES_URL)
                    .path("/clients/" + clientId)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 404) {
                throw new AccountException("Client not found: " + clientId, 404);
            }
            if (response.getStatus() >= 400) {
                throw new AccountException("Error verifying client in MS_CLIENTES (HTTP " + response.getStatus() + ")", 502);
            }
        } catch (AccountException e) {
            throw e;
        } catch (Exception e) {
            throw new AccountException("MS_CLIENTES is unreachable: " + e.getMessage(), 503);
        } finally {
            httpClient.close();
        }
    }

    private AccountResponseDTO toResponse(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                account.getType(),
                account.getClientId()
        );
    }

    private String generateUniqueAccountNumber() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String candidate = generateAccountNumber();
            if (accountRepository.findByAccountNumber(candidate).isEmpty()) {
                return candidate;
            }
        }
        throw new AccountException("Unable to generate account number", 500);
    }

    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder(ACCOUNT_NUMBER_LENGTH);
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
