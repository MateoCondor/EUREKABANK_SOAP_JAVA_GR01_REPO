package ec.edu.monster.model;

/**
 * Account entity para MS_CUENTAS.
 * IMPORTANTE: La FK a Client fue eliminada porque Client vive en otro microservicio (MS_CLIENTES).
 * En su lugar se guarda el clientId como Long simple. La integridad se mantiene a nivel
 * de aplicación: AccountService verifica la existencia del cliente llamando a MS_CLIENTES via HTTP.
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

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AccountType type;

    /**
     * ID del cliente dueño de esta cuenta.
     * No hay @ManyToOne porque Client vive en la BD de MS_CLIENTES.
     */
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    public Account() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
}
