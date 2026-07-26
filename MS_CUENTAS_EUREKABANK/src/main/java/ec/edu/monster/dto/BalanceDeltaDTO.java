package ec.edu.monster.dto;

import java.math.BigDecimal;

/**
 * DTO para el endpoint interno PUT /accounts/{id}/balance.
 * Usado por MS_TRANSACCIONES para actualizar saldos.
 * delta positivo = crédito (aumentar saldo)
 * delta negativo = débito  (disminuir saldo)
 */
public class BalanceDeltaDTO {
    private BigDecimal delta;

    public BalanceDeltaDTO() {}
    public BalanceDeltaDTO(BigDecimal delta) { this.delta = delta; }
    public BigDecimal getDelta() { return delta; }
    public void setDelta(BigDecimal delta) { this.delta = delta; }
}
