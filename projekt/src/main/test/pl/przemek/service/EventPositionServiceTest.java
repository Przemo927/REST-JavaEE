package pl.przemek.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.przemek.model.EventPosition;
import pl.przemek.repository.JpaEventPositionRepository;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class EventPositionServiceTest {
    private EventPositionService eventPositionService;
    private JpaEventPositionRepository eventPosRepository;
    private Logger logger;

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        eventPosRepository=mock(JpaEventPositionRepository.class);
        logger=mock(Logger.class);
        eventPositionService=new EventPositionService(logger,eventPosRepository);

    }

    @Test
    public void shouldExecuteMethodAddOfEventPositionRepository() throws Exception {
        EventPosition eventPosition=mock(EventPosition.class);
        eventPositionService.addEventPosition(eventPosition);
        verify(eventPosRepository).add(eventPosition);
    }

}