package pl.przemek.filter;

import org.junit.Before;
import org.junit.Test;
import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepositoryImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginFilterTest {

    private JpaUserRepositoryImpl userRepository;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private LoginFilter loginFilter;

    @Before
    public void setUp() throws Exception {
        userRepository=mock(JpaUserRepositoryImpl.class);
        loginFilter=spy(new LoginFilter(userRepository));
    }

    @Test
    public void shouldDoNothingWhenUserIsNotLoggedIn() throws Exception {
        request=mock(HttpServletRequest.class);
        response=mock(HttpServletResponse.class);
        FilterChain filterChain=mock(FilterChain.class);
        HttpSession session=mock(HttpSession.class);

        when(request.getUserPrincipal()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        loginFilter.doFilter(request,response,filterChain);

        verify(loginFilter,times(0)).LogoutIfInActiveStatus(isA(User.class),
                isA(HttpServletRequest.class),isA(HttpServletResponse.class));
        verify(loginFilter,times(0)).saveUserInSession(isA(HttpServletRequest.class),isA(User.class));
    }

    @Test
    public void shouldCheckStatusOfUserAndSaveUserOnSessionWhenUseIsLoggedInAndNotSavednOnSession() throws Exception {
        String userName="UserName";
        request=mock(HttpServletRequest.class);
        response=mock(HttpServletResponse.class);
        FilterChain filterChain=mock(FilterChain.class);
        HttpSession session=mock(HttpSession.class);
        Principal principal=mock(Principal.class);
        List<User> listOfUsers=new ArrayList<>();
        User user=new User();
        user.setActive(true);
        listOfUsers.add(user);

        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(userName);
        when(request.getSession()).thenReturn(session);
        when(userRepository.getUserByUsername(userName)).thenReturn(listOfUsers);
        doNothing().when(loginFilter).saveUserInSession(isA(HttpServletRequest.class),isA(User.class));
        loginFilter.doFilter(request,response,filterChain);

        verify(loginFilter).LogoutIfInActiveStatus(user,request,response);
        verify(loginFilter).saveUserInSession(request,user);
        verify(filterChain).doFilter(request,response);
    }

    @Test
    public void shouldDoNothingWhenUserIsLoggedInAndIsSavedOnSession() throws Exception {
        String userName="UserName";
        request=mock(HttpServletRequest.class);
        response=mock(HttpServletResponse.class);
        FilterChain filterChain=mock(FilterChain.class);
        HttpSession session=mock(HttpSession.class);
        Principal principal=mock(Principal.class);
        List<User> listOfUsers=new ArrayList<>();
        User user=new User();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getUserPrincipal()).thenReturn(principal);
        loginFilter.doFilter(request,response,filterChain);

        verify(loginFilter,times(0)).LogoutIfInActiveStatus(isA(User.class),
                isA(HttpServletRequest.class),isA(HttpServletResponse.class));
        verify(loginFilter,times(0)).saveUserInSession(isA(HttpServletRequest.class),isA(User.class));
    }
    @Test
    public void shouldInvalidateSessionAndSendRedirectWhenActiveStatusOfUserIsFalse() throws Exception{
        request=mock(HttpServletRequest.class);
        response=mock(HttpServletResponse.class);
        HttpSession session=mock(HttpSession.class);
        User user=new User();
        user.setActive(false);

        when(request.getSession()).thenReturn(session);
        loginFilter.LogoutIfInActiveStatus(user,request,response);

        verify(session).invalidate();
        verify(response).sendRedirect(anyString());

    }
    @Test
    public void shouldDoNothingWhenActiveStatusOfUserIsTrue() throws Exception {
        request=mock(HttpServletRequest.class);
        response=mock(HttpServletResponse.class);
        User user=new User();
        user.setActive(true);
        loginFilter.LogoutIfInActiveStatus(user,request,response);

        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

}