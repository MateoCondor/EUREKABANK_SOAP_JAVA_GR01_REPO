package ec.edu.monster.repository;

import ec.edu.monster.model.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClientRepository {

    @PersistenceContext(unitName = "ClientesPU")
    private EntityManager entityManager;

    public void create(Client client) {
        entityManager.persist(client);
    }

    public Client update(Client client) {
        return entityManager.merge(client);
    }

    public void delete(Client client) {
        if (entityManager.contains(client)) {
            entityManager.remove(client);
        } else {
            entityManager.remove(entityManager.merge(client));
        }
    }

    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Client.class, id));
    }

    public List<Client> findAll() {
        return entityManager.createQuery(
                "SELECT c FROM Client c ORDER BY c.id",
                Client.class
        ).getResultList();
    }

    public Optional<Client> findByDni(String dni) {
        TypedQuery<Client> query = entityManager.createQuery(
                "SELECT c FROM Client c WHERE c.dni = :dni",
                Client.class
        );
        query.setParameter("dni", dni);
        query.setMaxResults(1);
        List<Client> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
