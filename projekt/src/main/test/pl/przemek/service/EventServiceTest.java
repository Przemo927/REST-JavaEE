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
import static org.mockito.AdditionalMatchers.not;
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
        verify(eventRepo,times(1)).add(event);
    }

    @Test
    public void shouldUseTheSameEventObject() throws Exception {
        Event event1=mock(Event.class);
        Event event2=mock(Event.class);
        eventService.addEvent(event1);
        verify(eventRepo,times(1)).add(event1);
        verify(eventRepo,never()).add(event2);
    }

    @Test
    public void shouldExecuteMethodUpdate() throws Exception {
        Event event=mock(Event.class);
        eventService.updateEvent(event);
        verify(eventRepo).update(event);
    }

    @Test
    public void shouldUseTheSameEventObjectDuringUpdate() throws Exception {
        Event event1=mock(Event.class);
        Event event2=mock(Event.class);
        eventService.updateEvent(event1);
        verify(eventRepo,times(1)).update(event1);
        verify(eventRepo,never()).update(event2);
    }

    @Test
    public void shouldExecuteMethodRemoveOfEventRepository() throws Exception {
        eventService=new EventService(eventRepo);
        Event event=mock(Event.class);
        when(eventRepo.get(anyLong())).thenReturn(event);
        eventService.removeEventById(anyLong());
        verify(eventRepo).remove(event);
    }

    public Object[] eventId(){
        return $(12,10000,123456);
    }
    @Test
    @Parameters(method = "eventId")
    public void shouldUseTheSameValueOfId(long id) throws Exception {
        eventService=new EventService(eventRepo);
        eventService.removeEventById(id);
        verify(eventRepo,times(1)).get(id);
        verify(eventRepo,never()).get(not(eq(id)));
    }

    @Test
    public void shouldExecuteGetMethod() throws Exception {
        eventService=new EventService(eventRepo);
        eventService.getEvent(anyLong());
        verify(eventRepo).get(anyLong());
    }

    @Test
    @Parameters(method = "eventId")
    public void shouldUseTheSameValueOfIdMethodGetEvent(long id) throws Exception {
        eventService=new EventService(eventRepo);
        eventService.getEvent(id);
        verify(eventRepo,times(1)).get(id);
        verify(eventRepo,never()).get(not(eq(id)));
    }

    @Test
    public void shouldExecuteMethodGetAll() throws Exception {
        eventService=new EventService(eventRepo);
        eventService.getAllEvents();
        verify(eventRepo).getAll();
    }

    @Test
    public void shouldExecuteMethodGetByCity() throws Exception {
        eventService=new EventService(eventRepo);
        eventService.getEventsByCity(anyString());
        verify(eventRepo).getByCity(anyString());
    }

    public Object[] nameOfCity(){
        return $("miasto","miasto1","miastomiasto");
    }
    @Test
    @Parameters(method = "nameOfCity")
    public void shouldUseTheSameNameOfCity(String city) throws Exception {
        eventService=new EventService(eventRepo);
        eventService.getEventsByCity(city);
        verify(eventRepo,times(1)).getByCity(city);
        verify(eventRepo,never()).getByCity(city.toUpperCase());
        verify(eventRepo,never()).getByCity(not(eq(city)));
    }
    @Test
    public void shouldChangeToUnsignedIntegerWhenDistanceIsLessThanNull() throws Exception{
        int distance=-100;
        int unsignedDistance=100;
        List listOfEvents=mock(List.class);
        eventService=spy(new EventService(eventRepo));
        when(eventRepo.getAll()).thenReturn(listOfEvents);
        eventService.getEventByPosition(10.0, 20.0, distance);
        verify(eventService).getListOfEventInsideDistanceBufor(10.0,20.0,unsignedDistance,listOfEvents);

    }
    @Test
    public void shouldExecuteMethodGetListOfEventInsideDistanceBuffor() throws Exception {
        eventService=spy(new EventService(eventRepo));
        when(eventRepo.getAll()).thenReturn(new ArrayList<Event>());
        eventService.getEventByPosition(anyDouble(), anyDouble(), anyInt());
        verify(eventService,times(1)).getListOfEventInsideDistanceBufor(anyDouble(),anyDouble(),anyInt(),anyList());
    }

    @Test
    public void shouldReturnListWithEventsWhenCheckingDistanceMethodReturnTrue() throws Exception {
        eventService=spy(new EventService(eventRepo));
        Event event1=mock(Event.class);
        Event event2=mock(Event.class);
        Event event3=mock(Event.class);
        List<Event> listOfEvents= Arrays.asList(event1,event2,event3);
        doReturn(true,false,true).when(eventService).checkingDistance(anyDouble(),anyDouble(),anyDouble(), anyList());
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
    public void shouldReturnTrueWhenComputedDistanceIsShorterThanGivenDistance(double difference) throws Exception{
        eventService=spy(new EventService(eventRepo));
        List<EventPosition> listOfPositions=new ArrayList<EventPosition>();
        listOfPositions.add(mock(EventPosition.class));

        when(eventService.formulaToComputeDistance(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn(difference);

        assertTrue(eventService.checkingDistance(1,1,10,listOfPositions));


    }
    public Object[] differenceLongerandEqualThanGivenDistance() {
        return $(10,11,15,20,100);
    }

    @Test
    @Parameters(method ="differenceLongerandEqualThanGivenDistance")
    public void shouldReturFalseWhenComputedDistanceIsLongerOrEqualThanGivenDistance(double difference) {
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