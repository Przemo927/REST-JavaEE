package pl.przemek.rest;

import pl.przemek.model.EventPosition;
import pl.przemek.service.EventPositionService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.awt.*;

@Path("/position")
public class EventPositionEndPoint {

    private EventPositionService eventPositionService;

    public EventPositionEndPoint(){}

    @Inject
    public EventPositionEndPoint(EventPositionService eventPositionService){
        this.eventPositionService=eventPositionService;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addEventPosition(EventPosition position){
        eventPositionService.addEventPosition(position);
    }
}
