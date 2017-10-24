package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import pl.przemek.model.Event;
import pl.przemek.model.EventPosition;
import pl.przemek.repository.JpaEventRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class EventServiceTest {

    private JpaEventRepository eventRepo;
    private EventService eventService;

    @Rule
    public ExpectedException thrown=ExpectedException.none();
    @Before
    public void setUp(){
        eventRepo=mock(JpaEventRepository.class);
        eventService=new EventService(eventRepo);
    }
    @Test
    public void shouldExecuteMethodAdd() throws Exception {
        Event event=mock(Event.class);
        eventService.addEvent(event);
        verify(eventRepo).add(event);
    }

    @Test
    public void shouldExecuteMethodUpdate() throws Exception {
        Event event=mock(Event.class);
        eventService.updateEvent(event);
        verify(eventRepo).update(event);
    }

    @Test
    public void shouldExecuteMethodRemoveOfEventRepository() throws Exception {
        Event event=mock(Event.class);
        when(eventRepo.get(anyLong())).thenReturn(event);
        eventService.removeEventById(anyLong());
        verify(eventRepo).remove(event);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenIdIsNull() throws Exception {
        Long id=null;
        thrown.expect(NullPointerException.class);
        eventService.removeEventById(id);
    }
    @Test
    public void shouldUseTheSameValueOfId() throws Exception {
        long id=12345;
        eventService.removeEventById(id);
        verify(eventRepo).get(id);
        verify(eventRepo,never()).get(1);
        verify(eventRepo,never()).get(1234);
        verify(eventRepo,never()).get(12346);
    }

    @Test
    public void shouldExecuteGet() throws Exception {
        eventService.getEvent(anyLong());
        verify(eventRepo).get(anyLong());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenIdIsNull2() throws Exception {
        Long id=null;
        thrown.expect(NullPointerException.class);
        eventService.getEvent(id);
    }

    @Test
    public void shouldUseTheSameValueOfIdMethodGetEvent() throws Exception {
        long id=12345;
        eventService.getEvent(id);
        verify(eventRepo,never()).get(1);
        verify(eventRepo,never()).get(1234);
        verify(eventRepo,never()).get(12346);
    }

    @Test
    public void shouldExecuteMethodGetAll() throws Exception {
        eventService.getAllEvents();
        verify(eventRepo).getAll();
    }
    @Test
    public void shouldReturnEmptyListOfEvents() throws Exception {
        List<Event> listOfEvents=new ArrayList<>();
        assertEquals(listOfEvents,eventService.getAllEvents());
    }
    @Test
    public void shouldExecuteMethodGetByCity() throws Exception {
        eventService.getEventsByCity(anyString());
        verify(eventRepo).getByCity(anyString());
    }
    @Test
    public void shouldUseTheSameNameOfCity() throws Exception {
        String city="City";
        eventService.getEventsByCity(city);
        verify(eventRepo).getByCity(city);
        verify(eventRepo,never()).getByCity("city");
        verify(eventRepo,never()).getByCity("City1");
        verify(eventRepo,never()).getByCity(null);
    }
    @Test
    public void shouldExecuteMethodGetListOfEventInsideDistanceBuffor() throws Exception {
        EventService eventService=spy(new EventService(eventRepo));
        when(eventRepo.getAll()).thenReturn(new ArrayList<Event>());
        eventService.getEventByPosition(anyDouble(), anyDouble(), anyInt());
        verify(eventService).getListOfEventInsideDistanceBufor(anyDouble(),anyDouble(),anyInt(),anyList());
    }
    @Test
    public void shouldReturnEmptyListOfEventsInsideDistanceBuffor() throws Exception {
        List<Event> listOfEvents=new ArrayList<>();
        EventService eventService=spy(new EventService(eventRepo));
        when(eventRepo.getAll()).thenReturn(new ArrayList<Event>());
        assertEquals(listOfEvents,eventService.getEventByPosition(anyDouble(), anyDouble(), anyInt()));
    }

    @Test
    public void shouldReturnListWithEventsWhenCheckingDistanceMethodReturnTrue() throws Exception {
        EventService eventService=spy(new EventService(eventRepo));
        Event event1=mock(Event.class);
        Event event2=mock(Event.class);
        Event event3=mock(Event.class);
        List<Event> listOfEvents= Arrays.asList(event1,event2,event3);
        when(eventService.checkingDistance(anyDouble(),anyDouble(),anyInt(), anyList())).thenReturn(true,false, true);
        List<Event> listOfEventsInsidePosition=eventService.getListOfEventInsideDistanceBufor(1,2,3,listOfEvents);
        assertEquals(2,listOfEventsInsidePosition.size());
        assertTrue(listOfEvents.contains(event1));
        assertTrue(listOfEvents.contains(event3));

    }
    public Object[] differenceShorterThanGivenDistance() {
        return $(1,2,3,4,5,6,7,8,9);
    }

    @Test
    @Parameters(method ="differenceShorterThanGivenDistance")
    public void shouldReturnFalseWhenComputedDistanceIsShorterThanGivenDistance(double difference) throws Exception{
        List<EventPosition> listOfPositions=new ArrayList<EventPosition>();
        listOfPositions.add(mock(EventPosition.class));
        EventService eventService=spy(new EventService(eventRepo));
        when(eventService.formulaToComputeDistance(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn(difference);
        assertTrue(eventService.checkingDistance(1,1,10,listOfPositions));


    }
    public Object[] differenceLongerandEqualThanGivenDistance() {
        return $(10,11,15,20,100);
    }

    @Test
    @Parameters(method ="differenceLongerandEqualThanGivenDistance")
    public void shouldReturTrueWhenComputedDistanceIsLongerOrEqualThanGivenDistance(double difference) {
        List<EventPosition> listOfPositions=new ArrayList<EventPosition>();
        listOfPositions.add(mock(EventPosition.class));
        EventService eventService=spy(new EventService(eventRepo));
        when(eventService.formulaToComputeDistance(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn(difference);
        assertFalse(eventService.checkingDistance(1,1,10,listOfPositions));
    }

    public Object[] coordinates() {
        return $(new Object[]{0, 0, 0, 0},new Object[]{0, 0, 180, 180},new Object[]{0, 180, 0, 180},new Object[]{-180, -180, 0, 0});
    }

    @Test
    @Parameters(method = "coordinates")
    public void shouldAlwaysReturnZeroOrPositiveValues(double latCoordinateGivenByUser, double lngCoordinateGivenByUser,double latCoordinateOfEvent, double lngCoordinateOfEvent){
        assertTrue((eventService.formulaToComputeDistance(latCoordinateGivenByUser, lngCoordinateGivenByUser, latCoordinateOfEvent, lngCoordinateOfEvent)>=0));
    }
}