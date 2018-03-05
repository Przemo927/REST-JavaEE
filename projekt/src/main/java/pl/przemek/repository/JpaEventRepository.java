package pl.przemek.repository;


import pl.przemek.model.Event;

import java.util.List;

public interface JpaEventRepository {
    public void add(Event event);
    public void remove(Event event);
    public Event update(Event comment);
    public List<Event> getAll();
    public List<Event> getAllAndGroupBy();
    public Event get(long id);
    public List<Event> getByCity(String city);
    public List<String> getCitiesFromAllEvents();
}
