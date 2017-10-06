package pl.przemek.repository;


import pl.przemek.model.EventPosition;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaEventPositionRepositoryImpl implements JpaEventPositionRepository {

    @PersistenceContext
    private EntityManager em;

    @RolesAllowed({"admin","user"})
    @Override
    public void add(EventPosition position) {
      em.persist(position);
    }
    @RolesAllowed({"admin","user"})
    @Override
    public void remove(EventPosition position) {
    em.remove(em.merge(position));
    }

    @RolesAllowed({"admin","user"})
    @Override
    public EventPosition update(EventPosition position) {
        return em.merge(position);
    }

    @RolesAllowed({"admin","user"})
    @Override
    public List<EventPosition> getAll() {
        TypedQuery<EventPosition> getAllPositions=em.createNamedQuery("EventPosition.findAll",EventPosition.class);
        return getAllPositions.getResultList();
    }

    @RolesAllowed({"admin","user"})
    @Override
    public EventPosition get(long id) {
        return em.find(EventPosition.class,id);
    }
}
