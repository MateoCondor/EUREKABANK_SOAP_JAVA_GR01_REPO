package ec.edu.monster.dto;

import ec.edu.monster.model.AccountStatus;
import ec.edu.monster.model.AccountType;

public class AccountRequestDTO {
    private Long clientId;
    private AccountType type;
    private AccountStatus status;

    public AccountRequestDTO() {}
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
}
