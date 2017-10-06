package pl.przemek.repository;

import pl.przemek.model.Event;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaEventRepositoryImpl implements JpaEventRepository {

    @PersistenceContext
    EntityManager em;

    @RolesAllowed({"admin","user"})
    @Override
    public void add(Event event)
    {
        em.persist(event);
    }

    @RolesAllowed({"admin","user"})
    @Override
    public void remove(Event event) {
        em.remove(em.merge(event));
    }

    @RolesAllowed({"admin","user"})
    @Override
    public Event update(Event event) {
        Event updatedEvent=em.merge(event);
        return updatedEvent;
    }
    @RolesAllowed({"admin","user"})
    @Override
    public List<Event> getAll() {
        TypedQuery<Event> getAllEvents=em.createNamedQuery("Event.findAll",Event.class);
        return getAllEvents.getResultList();
    }

    @RolesAllowed({"admin","user"})
    @Override
    public Event get(long id) {
        Event event=em.find(Event.class,id);
        return event;
    }

    @RolesAllowed({"admin","user"})
    @Override
    public List<Event> getByCity(String city) {
        TypedQuery<Event> getEventByCity=em.createNamedQuery("Event.findByCity",Event.class);
        getEventByCity.setParameter("city",city);
        return getEventByCity.getResultList();
    }
}
