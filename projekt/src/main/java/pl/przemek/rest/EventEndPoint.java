package pl.przemek.rest;

import com.sun.org.apache.regexp.internal.RE;
import pl.przemek.model.Event;
import pl.przemek.model.User;
import pl.przemek.service.EventService;
import pl.przemek.wrapper.ResponseMessageWrapper;

import javax.ejb.Remove;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("/event")
public class EventEndPoint {

    private EventService eventservice;
    private HttpServletRequest request;
    private Logger logger;

    @Context
    UriInfo uriInfo;

    public EventEndPoint() {
    }
    @Inject
    public EventEndPoint(Logger logger,EventService eventservice,HttpServletRequest request) {
        this.logger=logger;
        this.eventservice = eventservice;
        this.request=request;
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam("id") long id) {
        Optional<Event> eventOptional=eventservice.getEvent(id);
        return eventOptional.map(event -> {
            return Response.ok(event).build();
        }).orElseGet(()->{
            logger.log(Level.SEVERE,"[EventEndPoint] getEvent() event wasn't found");
            return Response.status(Response.Status.NO_CONTENT).build();
        });
        }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents(@QueryParam("city") @DefaultValue("allCities") String city ) {
        List<Event> listOfEvents;
        if("allCities".equals(city)){
            listOfEvents=eventservice.getAllEvents();
        }else
            listOfEvents=eventservice.getEventsByCity(city);
        if(listOfEvents.isEmpty()) {
            logger.log(Level.SEVERE, "[EventEndPoint] getEvents() events wasn't found");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(listOfEvents).build();
    }

    @GET
    @Path("/position")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventsByPosition(@QueryParam("x") double x, @QueryParam("y") double y,@QueryParam("distance") int distance ) {
        List<Event> listOfEventsGetByPosition=eventservice.getEventByPosition(x,y,distance);
        if(listOfEventsGetByPosition.isEmpty()) {
            logger.log(Level.SEVERE, "[EventEndPoint] getEventsByPosition() events wasn't found");
            Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(listOfEventsGetByPosition).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEvent(Event event){
        User user=(User)request.getSession().getAttribute("user");
        if(user!=null && event!=null) {
            event.setUser(user);
            eventservice.addEvent(event);
            return Response.created(URI.create(uriInfo.getAbsolutePath()+"/"+event.getId())).build();
        }else {
            logger.log(Level.SEVERE, "[EventEndPoint] addEvent() event wasn't added user="+user+" event="+event);
            return Response.status(Response.Status.UNAUTHORIZED).entity(ResponseMessageWrapper.wrappMessage("Logged user wasn't found")).build();
        }
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEvent(Event event){
        if(event!=null) {
            eventservice.updateEvent(event);
            return Response.ok(ResponseMessageWrapper.wrappMessage("Event was updated")).build();
        }else {
            logger.log(Level.SEVERE, "[EventEndPoint] updateEvent() event is null");
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeEvent(@PathParam("id") long id){
        eventservice.removeEventById(id);
        return Response.ok(ResponseMessageWrapper.wrappMessage("Event was removed")).build();
    }

    @GET
    @Path("/cities")
    @Produces
    public Response getCitiesFromAllEvents(){
        List<String> listOfCities=eventservice.getCitiesFromAllEvents();
        if(listOfCities.isEmpty()) {
            logger.log(Level.SEVERE, "[EventEndPoint] getCitiesFromAllEvents() cities weren't found");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(listOfCities).build();
    }

}
