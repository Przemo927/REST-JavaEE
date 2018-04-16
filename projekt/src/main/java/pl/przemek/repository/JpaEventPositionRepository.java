package pl.przemek.repository;


import pl.przemek.model.EventPosition;

import java.util.List;

public interface JpaEventPositionRepository {
    public void add(EventPosition position);
    public void remove(EventPosition position);
    public EventPosition update(EventPosition position);
    public List<EventPosition> getAll();
    public EventPosition get(long id);
}
