package pl.przemek.repository;

import pl.przemek.model.Event;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

@Stateless
public class JpaEventRepositoryImpl extends JpaRepository<Event> implements JpaEventRepository {

    private final static String QUANTITY_OF_EVENTS="SELECT count(*) FROM event";

    @RolesAllowed({"admin","user"})
    @Override
    public void remove(Event event) {
        em.remove(em.merge(event));
    }

    @Override
    public List<Event> getAllAndGroupBy() {
        return null;
    }

    @RolesAllowed({"admin","user"})
    @Override
    public List<Event> getByCity(String city) {
        TypedQuery<Event> getEventByCity=em.createNamedQuery("Event.findByCity",Event.class);
        getEventByCity.setParameter("city",city+"%");
        return getEventByCity.getResultList();
    }

    @RolesAllowed({"admin","user"})
    @Override
    public List<String> getCitiesFromAllEvents() {
        TypedQuery<String> getCitiesFromAllEvents=em.createNamedQuery("Event.selectAllCities",String.class);
        List<String> listOfCities=getCitiesFromAllEvents.getResultList();
        listOfCities.sort(Comparator.naturalOrder());
        return listOfCities;
    }
    @RolesAllowed({"admin","user"})
    @Override
    public BigInteger getQuantityOfEvents() {
        Query query=em.createNativeQuery(QUANTITY_OF_EVENTS);
        return (BigInteger)query.getSingleResult();
    }

    @RolesAllowed({"admin","user"})
    @Override
    public List<Event> getWithLimit(int begin, int quantity) {
        TypedQuery<Event> query=em.createNamedQuery("Event.findAllWithLimit",Event.class);
        query.setParameter("begin",begin);
        query.setParameter("quantity",quantity);
        return query.getResultList();
    }
}
