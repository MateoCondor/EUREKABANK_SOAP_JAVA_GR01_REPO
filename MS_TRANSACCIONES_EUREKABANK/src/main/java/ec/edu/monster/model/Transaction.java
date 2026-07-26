package ec.edu.monster.model;

/**
 * Transaction entity para MS_TRANSACCIONES.
 * IMPORTANTE: Las FK a Account fueron eliminadas (Account vive en MS_CUENTAS).
 * Se usan Long simples: sourceAccountId y targetAccountId.
 * La integridad se mantiene via HTTP: MS_TRANSACCIONES llama a MS_CUENTAS antes de registrar.
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    /** ID de la cuenta origen (sin FK real — Account vive en MS_CUENTAS) */
    @Column(name = "source_account_id", nullable = false)
    private Long sourceAccountId;

    /** ID de la cuenta destino (nullable para depósitos y retiros) */
    @Column(name = "target_account_id")
    private Long targetAccountId;

    @Column(precision = 15, scale = 2)
    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", length = 10)
    private TransferType transferType;

    @Column(length = 250)
    private String description;

    public Transaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Long getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(Long sourceAccountId) { this.sourceAccountId = sourceAccountId; }
    public Long getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(Long targetAccountId) { this.targetAccountId = targetAccountId; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public TransferType getTransferType() { return transferType; }
    public void setTransferType(TransferType transferType) { this.transferType = transferType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
