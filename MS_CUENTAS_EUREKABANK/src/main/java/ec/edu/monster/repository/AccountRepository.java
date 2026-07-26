package ec.edu.monster.repository;

import ec.edu.monster.model.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountRepository {

    @PersistenceContext(unitName = "CuentasPU")
    private EntityManager entityManager;

    public void create(Account account) {
        entityManager.persist(account);
    }

    public Account update(Account account) {
        return entityManager.merge(account);
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    public List<Account> findAll() {
        return entityManager.createQuery(
                "SELECT a FROM Account a ORDER BY a.id",
                Account.class
        ).getResultList();
    }

    /**
     * Busca cuentas por el clientId (campo simple Long, sin FK cruzada de schema).
     */
    public List<Account> findByClientId(Long clientId) {
        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a WHERE a.clientId = :clientId ORDER BY a.id",
                Account.class
        );
        return query.setParameter("clientId", clientId).getResultList();
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a WHERE a.accountNumber = :accountNumber",
                Account.class
        );
        query.setParameter("accountNumber", accountNumber);
        query.setMaxResults(1);
        List<Account> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
