package pl.przemek.service;

import pl.przemek.model.EventPosition;
import pl.przemek.repository.JpaEventPositionRepository;

import javax.inject.Inject;


public class EventPositionService {


    private JpaEventPositionRepository eventposrepo;

    @Inject
    public EventPositionService(JpaEventPositionRepository eventposrepo){
        this.eventposrepo=eventposrepo;
    }
    public EventPositionService () {
        this.eventposrepo=null;
    }

    public void addEventPosition(EventPosition position){

        eventposrepo.add(position);
    }
}
