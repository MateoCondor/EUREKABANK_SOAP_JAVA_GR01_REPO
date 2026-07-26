package ec.edu.monster.dto;

import ec.edu.monster.model.TransferType;
import java.math.BigDecimal;

public class TransferDTO {
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private TransferType transferType;
    private String description;

    public TransferDTO() {}
    public Long getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(Long sourceAccountId) { this.sourceAccountId = sourceAccountId; }
    public Long getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(Long targetAccountId) { this.targetAccountId = targetAccountId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransferType getTransferType() { return transferType; }
    public void setTransferType(TransferType transferType) { this.transferType = transferType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
