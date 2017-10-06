package pl.przemek.service;

import pl.przemek.model.EventPosition;
import pl.przemek.repository.JpaEventPositionRepository;

import javax.inject.Inject;


public class EventPositionService {

    @Inject
    private JpaEventPositionRepository eventposrepo;

    public void addEventPosition(EventPosition position){
        eventposrepo.add(position);
    }
}
