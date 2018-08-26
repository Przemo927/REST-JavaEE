package pl.przemek.service;


import pl.przemek.model.Event;
import pl.przemek.model.EventPosition;
import pl.przemek.model.SecurityKey;
import pl.przemek.repository.JpaEventRepository;
import sun.rmi.runtime.Log;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EventService {

    private JpaEventRepository eventRepo;
    private Logger logger;

    @Inject
    public EventService(Logger logger,JpaEventRepository eventRepo){
        this.logger=logger;
        this.eventRepo=eventRepo;
    }
    public EventService(){
    }


    public void addEvent(Event event){
        if(event==null){
            logger.log(Level.SEVERE,"[EventService] addEvent() event is null");
        }else{
            eventRepo.add(event);
        }
    }

    public void updateEvent(Event event){
        if(event==null){
            logger.log(Level.SEVERE,"[EventService] updateEvent() event is null");
        }else{
            eventRepo.update(event);
        }
    }

    public void removeEventById(long id){
            Event event=eventRepo.get(Event.class,id);
            if(event==null) {
                logger.log(Level.WARNING,"[EventService] removeEventById() event wasn't found");
            } else {
                eventRepo.remove(event);
            }
    }
    public Optional<Event> getEvent(long id){
        Event event=eventRepo.get(Event.class,id);
        return Optional.ofNullable(event);
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
        for (EventPosition aListOfEventPosition : listOfEventPosition) {
            computedDistance = computeDistance(latCoordinate, lngCoordinate, aListOfEventPosition.getxCoordinate(), aListOfEventPosition.getyCoordinate());
            if (distance > computedDistance) {
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
