package pl.przemek.service;

import pl.przemek.model.EventPosition;
import pl.przemek.repository.JpaEventPositionRepository;
import sun.rmi.runtime.Log;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EventPositionService {


    private JpaEventPositionRepository eventposrepo;
    private Logger logger;

    @Inject
    public EventPositionService(Logger logger,JpaEventPositionRepository eventposrepo){
        this.eventposrepo=eventposrepo;
        this.logger=logger;
    }
    public EventPositionService () {

    }

    public void addEventPosition(EventPosition position){
        if(position==null){
            logger.log(Level.SEVERE,"[EventPositionService] addEventPosition() position is null");
        }else{
            eventposrepo.add(position);
        }
    }
}
