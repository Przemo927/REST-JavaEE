package pl.przemek.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.Principal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginEndPointTest {
    private final static String homePath="/projekt/index.html#!/home";

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    LoginEndPoint loginEndPoint=new LoginEndPoint();


    @Test
    public void shouldRedirectToHomePathWhenSomeUserIsLoggedIn() throws Exception {
        Principal principal=mock(Principal.class);

        when(request.getUserPrincipal()).thenReturn(principal);
        loginEndPoint.login(request,response);

        verify(response).sendRedirect(homePath);

    }

    @Test
    public void shouldReturnErrorWhenSomeUserIsNotLoggedIn() throws Exception{
        when(request.getUserPrincipal()).thenReturn(null);
        loginEndPoint.login(request,response);
        verify(response).sendError(403);
    }

}