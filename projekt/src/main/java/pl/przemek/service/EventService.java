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
        try {
            eventRepo.add(event);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] addEvent() event="+event,e);
        }
    }

    public void updateEvent(Event event){
        try {
            eventRepo.update(event);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] updateEvent() event="+event,e);
        }
    }

    public void removeEventById(long id){
        try {
            Event event=eventRepo.get(Event.class,id);
            if(event==null)
                logger.log(Level.WARNING,"[EventService] removeEventById() event wasn't found");
            else
                eventRepo.remove(event);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] removeEventById() id="+id,e);
        }
    }
    public Optional<Event> getEvent(long id){
        Event event=null;
        try {
            event=eventRepo.get(Event.class,id);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] getEvent() id="+id,e);
            return Optional.empty();
        }
        return Optional.of(event);
    }

    public List<Event> getAllEvents(){
        List<Event> listOfEvents=null;
        try {
            listOfEvents=eventRepo.getAll("Event.findAll",Event.class);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] getAllEvents()",e);
            return Collections.emptyList();
        }
        return listOfEvents;
    }

    public List<Event> getEventsByCity(String city){
        List<Event> listOfEvents=null;
        try {
            listOfEvents=eventRepo.getByCity(city);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] getEventsByCity() name of city="+city,e);
            return Collections.emptyList();
        }
        return listOfEvents;
    }

    public List<Event> getEventByPosition(double latCoordinate, double lngCoordinate, int distance){
        List<Event> list=null;
        if(distance<0){
            distance=distance*(-1);
        }
        try {
            List<Event> lisOfEvents=eventRepo.getAll("Event.findAll",Event.class);
            list=getListOfEventInsideDistanceBufor(latCoordinate,lngCoordinate,distance,lisOfEvents);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] getEventByPosition() lat="+latCoordinate+
                    " long="+lngCoordinate+" distance="+distance,e);
            return Collections.emptyList();
        }
        return list;
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
        List<String> allCities=null;
        try {
            allCities=eventRepo.getCitiesFromAllEvents();
        }catch (Exception e){
            logger.log(Level.SEVERE,"[EventService] getCitiesFromAllEvents()",e);
            return Collections.emptyList();
        }
         return allCities;
    }
}
