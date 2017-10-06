package pl.przemek.rest;

import pl.przemek.model.Event;
import pl.przemek.model.User;
import pl.przemek.service.EventService;

import javax.ejb.Remove;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/event")
public class EventEndPoint {

    private EventService eventservice;


    private HttpServletRequest request;

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
    public Event getEvent(@PathParam("id") long id) {
        return eventservice.getEvent(id);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getEvents(@QueryParam("city") @DefaultValue("all") String city ) {
        if("all".equals(city)){
            return eventservice.getAllEvents();
        }
        return eventservice.getEventsByCity(city);
    }

    @GET
    @Path("/position")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getEventsByPosition(@QueryParam("x") double x, @QueryParam("y") double y,@QueryParam("distance") int distance ) {
            return eventservice.getEventByPosition(x,y,distance);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addEvent(Event event){
        User user=(User)request.getSession().getAttribute("user");
        event.setUser(user);
        eventservice.addEvent(event);
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateEvent(Event event){
        eventservice.updateEvent(event);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeEvent(@PathParam("id") long id){
        Event event=eventservice.getEvent(id);
        eventservice.removeEvent(event);
    }
}
