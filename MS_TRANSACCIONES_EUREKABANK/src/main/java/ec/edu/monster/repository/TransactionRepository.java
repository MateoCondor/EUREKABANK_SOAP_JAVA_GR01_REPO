package ec.edu.monster.repository;

import ec.edu.monster.model.Transaction;
import ec.edu.monster.model.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TransactionRepository {

    @PersistenceContext(unitName = "TransaccionesPU")
    private EntityManager entityManager;

    public void create(Transaction transaction) {
        entityManager.persist(transaction);
    }

    /**
     * Busca transacciones donde la cuenta sea origen o destino.
     * JPQL adaptado: usa sourceAccountId y targetAccountId (Long) en lugar de sourceAccount.id.
     */
    public List<Transaction> findByAccountId(Long accountId) {
        TypedQuery<Transaction> query = entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.sourceAccountId = :accountId "
                        + "OR t.targetAccountId = :accountId ORDER BY t.date DESC",
                Transaction.class
        );
        return query.setParameter("accountId", accountId).getResultList();
    }

    /**
     * Suma el monto operado por una cuenta origen para un tipo dado dentro de un rango de fechas.
     * Usado para validar límites diarios.
     */
    public BigDecimal sumDailyAmount(Long accountId, TransactionType type,
            LocalDateTime startOfDay, LocalDateTime endOfDay) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t "
                        + "WHERE t.sourceAccountId = :accountId "
                        + "AND t.type = :type "
                        + "AND t.date >= :startOfDay "
                        + "AND t.date < :endOfDay",
                BigDecimal.class
        );
        query.setParameter("accountId", accountId);
        query.setParameter("type", type);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }
}
