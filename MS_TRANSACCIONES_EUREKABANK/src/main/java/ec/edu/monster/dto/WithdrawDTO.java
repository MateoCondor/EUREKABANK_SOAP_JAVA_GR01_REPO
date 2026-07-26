package ec.edu.monster.dto;

import java.math.BigDecimal;

public class WithdrawDTO {
    private Long accountId;
    private BigDecimal amount;
    private String description;

    public WithdrawDTO() {}
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
