package pl.przemek.rest;

import org.junit.Before;
import org.junit.Test;
import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.service.DiscoveryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DiscoveryEndPointTest {
    private DiscoveryService discoveryService;
    private HttpServletRequest request;
    private DiscoveryEndPoint discoveryEndPoint;

    @Before
    public void setUp(){
        discoveryService=mock(DiscoveryService.class);
        request=mock(HttpServletRequest.class);
        discoveryEndPoint=new DiscoveryEndPoint(discoveryService,request);
    }
    @Test
    public void shouldAddUserToTheDiscovery() throws Exception {
        Discovery discovery=mock(Discovery.class);
        User user=mock(User.class);
        HttpSession session=mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(user);
        discoveryEndPoint.addDiscovery(discovery);

        verify(discovery).setUser(user);
    }

    @Test
    public void shouldExecuteMethoAddDiscoveryOfDiscoveryService() throws Exception {

        Discovery discovery=mock(Discovery.class);
        User user=mock(User.class);
        HttpSession session=mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        discoveryEndPoint.addDiscovery(discovery);

        verify(discoveryService).addDiscovery(discovery);


    }

}