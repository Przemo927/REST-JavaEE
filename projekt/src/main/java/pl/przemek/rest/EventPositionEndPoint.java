package pl.przemek.rest;

import pl.przemek.model.EventPosition;
import pl.przemek.service.EventPositionService;
import pl.przemek.wrapper.ResponseMessageWrapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;

@Path("/eventPosition")
public class EventPositionEndPoint {

    private EventPositionService eventPositionService;
    private final static ResponseMessageWrapper mw=new ResponseMessageWrapper();

    public EventPositionEndPoint(){}

    @Inject
    public EventPositionEndPoint(EventPositionService eventPositionService){
        this.eventPositionService=eventPositionService;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEventPosition(EventPosition position){
        eventPositionService.addEventPosition(position);
        return Response.ok(mw.wrappMessage("Event position was added")).build();
    }
}
