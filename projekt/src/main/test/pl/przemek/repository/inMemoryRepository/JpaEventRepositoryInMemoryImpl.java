package pl.przemek.repository.inMemoryRepository;

import pl.przemek.model.Event;
import pl.przemek.repository.JpaEventRepository;

import java.util.ArrayList;
import java.util.List;

public class JpaEventRepositoryInMemoryImpl implements JpaEventRepository {

    private List<Event> listOfEvents;

    public JpaEventRepositoryInMemoryImpl(){
        listOfEvents=new ArrayList<>();
    }
    @Override
    public void add(Event event) {
        listOfEvents.add(event);
    }

    @Override
    public void remove(Event event) {
        listOfEvents.remove(event);
    }

    @Override
    public Event update(Event event) {
        for(int i=0;i<listOfEvents.size();i++){
            if(event.getId()==listOfEvents.get(i).getId()){
                listOfEvents.set(i,event);
            }
        }
        return event;
    }

    @Override
    public List<Event> getAll(String nameOfQuery, Class<Event> clazz) {
        return null;
    }

    @Override
    public List<Event> getAllAndGroupBy() {
        return null;
    }

    @Override
    public Event get(Class<Event> clazz, long id) {
        return null;
    }

    @Override
    public List<Event> getByCity(String city) {
        return null;
    }

    @Override
    public List<String> getCitiesFromAllEvents() {
        return null;
    }
}
