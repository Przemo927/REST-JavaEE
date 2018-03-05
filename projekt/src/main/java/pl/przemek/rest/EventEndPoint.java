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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/event")
public class EventEndPoint {

    private EventService eventservice;
    private HttpServletRequest request;
    private final static ResponseMessageWrapper mw=new ResponseMessageWrapper();

    public EventEndPoint() {
    }
    @Inject
    public EventEndPoint(EventService eventservice,HttpServletRequest request) {
        this.eventservice = eventservice;
        this.request=request;
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam("id") long id) {
        Event event=eventservice.getEvent(id);
        if(event==null){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(event).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents(@QueryParam("city") @DefaultValue("allCities") String city ) {
        List<Event> listOfEvents;
        if("allCities".equals(city)){
            listOfEvents=eventservice.getAllEvents();
        }else
            listOfEvents=eventservice.getEventsByCity(city);
        if(listOfEvents.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok(listOfEvents).build();
    }

    @GET
    @Path("/position")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventsByPosition(@QueryParam("x") double x, @QueryParam("y") double y,@QueryParam("distance") int distance ) {
        List<Event> listOfEventsGetByPosition=eventservice.getEventByPosition(x,y,distance);
        if(listOfEventsGetByPosition.isEmpty())
            Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok(listOfEventsGetByPosition).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEvent(Event event){
        User user=(User)request.getSession().getAttribute("user");
        if(user!=null) {
            event.setUser(user);
            eventservice.addEvent(event);
            return Response.ok().build();
        }
        return Response.ok(mw.wrappMessage("User isn't logged")).build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEvent(Event event){
        eventservice.updateEvent(event);
        return Response.ok(mw.wrappMessage("Event was updated")).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeEvent(@PathParam("id") long id){
        eventservice.removeEventById(id);
        return Response.ok(mw.wrappMessage("Event was removed")).build();
    }

    @GET
    @Path("/cities")
    @Produces
    public Response getCitiesFromAllEvents(){
        List<String> listOfCities=eventservice.getCitiesFromAllEvents();
        if(listOfCities.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok(listOfCities).build();
    }

}
