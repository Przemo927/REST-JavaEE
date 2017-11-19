package pl.przemek.rest;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.przemek.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class HomeEndPointTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private HomeEndPoint homeEndPoint=new HomeEndPoint();

    private final static String loginPath= "/projekt/api/login";
    private final static String logoutPath= "/projekt/api/logout";

    @Test
    public void shouldReturnJsonWithInformationThatAdminIsLogged() throws Exception {
        Principal principal=mock(Principal.class);
        HttpSession session=mock(HttpSession.class);
        User user=mock(User.class);

        when(request.getUserPrincipal()).thenReturn(principal);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(user);
        when(request.isUserInRole("admin")).thenReturn(true);
        JSONObject jsonObject=homeEndPoint.loginStatus();

        assertEquals("Logout",jsonObject.get("name"));
        assertEquals("admin",jsonObject.get("role"));
        assertEquals(logoutPath,jsonObject.get("path"));
    }

    @Test
    public void shouldReturnJsonWithInformationThatSomeUserIsLogged() throws Exception {
        Principal principal=mock(Principal.class);
        HttpSession session=mock(HttpSession.class);
        User user=mock(User.class);

        when(request.getUserPrincipal()).thenReturn(principal);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(user);
        when(request.isUserInRole("user")).thenReturn(true);
        JSONObject jsonObject=homeEndPoint.loginStatus();

        assertEquals("Logout",jsonObject.get("name"));
        assertEquals("user",jsonObject.get("role"));
        assertEquals(logoutPath,jsonObject.get("path"));
    }

    @Test
    public void shouldReturnJsonWithInformationThatNobodyIsLogged(){

        when(request.getUserPrincipal()).thenReturn(null);
        JSONObject jsonObject=homeEndPoint.loginStatus();

        assertEquals("Login",jsonObject.get("name"));
        assertEquals(loginPath,jsonObject.get("path"));
    }

}