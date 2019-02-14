package pl.przemek.repository;


import pl.przemek.model.Event;

import java.math.BigInteger;
import java.util.List;

public interface JpaEventRepository {
    public void add(Event event);
    public void remove(Event event);
    public Event update(Event comment);
    public List<Event> getAll(String nameOfQuery,Class<Event> clazz);
    public List<Event> getAllAndGroupBy();
    public Event get(Class<Event> clazz,long id);
    public List<Event> getByCity(String city);
    public List<String> getCitiesFromAllEvents();
    public BigInteger getQuantityOfEvents();
    public List<Event> getWithLimit(int begin, int quantity);
}
