package ec.edu.monster.dto;

import java.math.BigDecimal;

/**
 * DTO interno para transportar datos mínimos de cuenta entre métodos de TransactionService.
 * No es un DTO de respuesta al cliente — solo se usa internamente para parsear la respuesta HTTP de MS_CUENTAS.
 */
public class AccountInfoDTO {
    private final Long id;
    private final BigDecimal balance;

    public AccountInfoDTO(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public BigDecimal getBalance() { return balance; }
}
