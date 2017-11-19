package pl.przemek.rest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.przemek.model.Event;
import pl.przemek.model.User;
import pl.przemek.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class EventEndPointTest {

    EventService eventService;
    HttpServletRequest request;
    EventEndPoint eventEndPoint;

    @Before
    public void setUp() throws Exception {
        eventService=mock(EventService.class);
        request=mock(HttpServletRequest.class);
        eventEndPoint=new EventEndPoint(eventService,request);
    }

    @Test
    public void shouldExecuteGetAllEvenentsWhenNameOfCityGivenAsParameterIsEqualAllCities() throws Exception {
        String city="allCities";
        eventEndPoint.getEvents(city);
        verify(eventService).getAllEvents();
    }

    public Object[] namesOfCities() {
        return $("Kraków","Olsztyn","Warszawa","alCities");
    }

    @Parameters(method ="namesOfCities")
    @Test
    public void shouldExecuteGetEventsByCityWhenNameOfCityGivenAsParameterIsNotEqualAllCities(String city) throws Exception {
        eventEndPoint.getEvents(city);
        verify(eventService).getEventsByCity(city);
    }

    public Object[] coordinates() {
        return $(new Double[] {1.0,2.0,3.0}, new Double[] {0.0,0.0,0.0},new Double[] {100.0,200.0,300.0}
                 );
    }

    @Parameters(method ="coordinates")
    @Test
    public void shouldExecuteGetEventByPositionMethodWitheSameParameters(Double x, Double y, Double distance) throws Exception {
        eventEndPoint.getEventsByPosition(x,y,distance.intValue());
        verify(eventService).getEventByPosition(x,y,distance.intValue());
    }
    @Test
    public void shouldAddEventWithGivenUser() throws Exception {
        HttpSession httpSession=mock(HttpSession.class);
        User user=mock(User.class);
        Event event=new Event();

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("user")).thenReturn(user);
        eventEndPoint.addEvent(event);
        assertEquals(user,event.getUser());

    }

    @Test
    public void getEventsByPosition() throws Exception {
    }

}