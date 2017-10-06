package pl.przemek.service;


import pl.przemek.model.Event;
import pl.przemek.model.EventPosition;
import pl.przemek.repository.JpaEventRepository;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EventService {

    @Inject
    private JpaEventRepository eventrepo;

    public void addEvent(Event event){
        eventrepo.add(event);
    }
    public Event updateEvent(Event event){
        return eventrepo.update(event);
    }
    public void removeEvent(Event event){
        eventrepo.remove(event);
    }
    public Event getEvent(long id){
        return eventrepo.get(id);
    }
    public List<Event> getAllEvents(){
        return eventrepo.getAll();
    }
    public List<Event> getEventsByCity(String city){
        return eventrepo.getByCity(city);
    }

    public List<Event> getEventByPosition(double x, double y, int distance){
        List<Event> lisOfEvents=eventrepo.getAll();
        return getListOfEventInsideDistanceBufor(x,y,distance,lisOfEvents);
    }
    private List<Event> getListOfEventInsideDistanceBufor(double x, double y,int distance, List<Event> listOfEvent){
        List<Event> listOfEventAfterChecking=listOfEvent.stream().parallel().filter(event->checkingDistance(x,y,distance,event.getEventPosition()))
        .collect(Collectors.toList());
        return listOfEventAfterChecking;
    }
    private static boolean checkingDistance(double x1, double y1, int distance, List<EventPosition> listOfEventPosition){
        double computedDistance;
        for(int i=0;i<listOfEventPosition.size();i++){
            computedDistance=patternToComputeDistance(x1,y1,listOfEventPosition.get(i).getxCoordinate(),listOfEventPosition.get(i).getyCoordinate());
            if(distance>computedDistance){
                return true;
            }
        }
        return false;

    }
    private static double patternToComputeDistance(double x1, double y1,double x2, double y2){
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(Math.cos((x1*Math.PI)/180)*(y2-y1),2))*40075.704/360;
    }
}
