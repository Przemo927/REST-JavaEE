package pl.przemek.repository.inMemoryRepository;

import pl.przemek.model.Event;
import pl.przemek.model.EventPosition;
import pl.przemek.model.User;
import pl.przemek.repository.JpaEventRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

public class JpaEventRepositoryInMemoryImpl implements JpaEventRepository {

    private List<Event> listOfEvents;

    public JpaEventRepositoryInMemoryImpl(){
        listOfEvents=new ArrayList<>();
        populateListOfEvents();
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
        return listOfEvents;
    }

    @Override
    public List<Event> getAllAndGroupBy() {
        return null;
    }

    @Override
    public Event get(Class<Event> clazz, long id) {
        for (Event event : listOfEvents) {
            if (event.getId() == id)
                return event;
        }
        return null;
    }

    @Override
    public List<Event> getByCity(String city) {
        List<Event> temporaryLis=new ArrayList<>();
        listOfEvents.forEach(event -> {
            if(event.getNameOfCity().equals(city))
                temporaryLis.add(event);
        });
        return temporaryLis;
    }

    @Override
    public List<String> getCitiesFromAllEvents() {
        List<String> temporaryList=new ArrayList<>();
        listOfEvents.forEach(event -> {
            temporaryList.add(event.getNameOfCity());
        });
        return temporaryList;
    }

    @Override
    public BigInteger getQuantityOfEvents() {
        return null;
    }

    @Override
    public List<Event> getWithLimit(int begin, int quantity) {
        return null;
    }

    private void populateListOfEvents(){
        for(int i=0;i<10;i++){
            Event event=new Event();
            event.setTimestamp(new Timestamp(new Date().getTime()));
            event.setNameOfCity("name"+i);
            event.setUser(new User());
            event.setId(i);
            event.setEventPosition(Collections.singletonList(new EventPosition()));
            listOfEvents.add(event);
        }
    }
}
