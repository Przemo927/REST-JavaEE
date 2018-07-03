package pl.przemek.service;


import pl.przemek.model.Event;
import pl.przemek.model.EventPosition;
import pl.przemek.repository.JpaEventRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class EventService {

    private JpaEventRepository eventRepo;

    @Inject
    public EventService(JpaEventRepository eventRepo){
        this.eventRepo=eventRepo;
    }
    public EventService(){
        this.eventRepo=null;
    }


    public void addEvent(Event event){
        eventRepo.add(event);
    }

    public void updateEvent(Event event){
        eventRepo.update(event);
    }

    public void removeEventById(long id){
        Event event=eventRepo.get(Event.class,id);
        eventRepo.remove(event);
    }
    public Event getEvent(long id){
        return eventRepo.get(Event.class,id);
    }

    public List<Event> getAllEvents(){
        return eventRepo.getAll("Event.findAll",Event.class);
    }

    public List<Event> getEventsByCity(String city){
        return eventRepo.getByCity(city);
    }

    public List<Event> getEventByPosition(double latCoordinate, double lngCoordinate, int distance){
        if(distance<0){
            distance=distance*(-1);
        }
        List<Event> lisOfEvents=eventRepo.getAll("Event.findAll",Event.class);
        return getListOfEventInsideDistanceBufor(latCoordinate,lngCoordinate,distance,lisOfEvents);
    }
    List<Event> getListOfEventInsideDistanceBufor(double latCoordinate, double lngCoordinate, int distance, List<Event> listOfEvent){
        return listOfEvent.stream().parallel().filter(event->checkingDistance(latCoordinate,lngCoordinate,distance,event.getEventPosition()))
        .collect(Collectors.toList());
    }
    boolean checkingDistance(double latCoordinate, double lngCoordinate, double distance, List<EventPosition> listOfEventPosition){
        double computedDistance;
        for(int i=0;i<listOfEventPosition.size();i++){
            computedDistance=computeDistance(latCoordinate,lngCoordinate,listOfEventPosition.get(i).getxCoordinate(),listOfEventPosition.get(i).getyCoordinate());
            if(distance>computedDistance){
                return true;
            }
        }

        return false;
    }
    double computeDistance(double latCoordinateGivenByUser, double lngCoordinateGivenByUser,double latCoordinateOfEvent, double lngCoordinateOfEvent){
        return Math.sqrt(Math.pow(latCoordinateGivenByUser-latCoordinateOfEvent,2)+Math.pow(Math.cos((latCoordinateGivenByUser*Math.PI)/180)*(lngCoordinateOfEvent-lngCoordinateGivenByUser),2))*40075.704/360;
    }

    public List<String> getCitiesFromAllEvents(){
         return eventRepo.getCitiesFromAllEvents();
    }
}
