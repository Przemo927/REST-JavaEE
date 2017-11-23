package pl.przemek.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.przemek.model.User;
import pl.przemek.model.VoteType;
import pl.przemek.service.VoteService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import java.net.URI;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class VoteEndPointTest {

    private VoteService voteService;
    private HttpServletRequest request;
    private VoteEndPoint voteEndPoint;
    @Before
    public void setUp() throws Exception {
        voteService=mock(VoteService.class);
        request=mock(HttpServletRequest.class);
        voteEndPoint=new VoteEndPoint(voteService,request);

    }

    @Test
    public void shouldReturnResponseObjectAndNotUpdateVoteWhenUserIsNotLoggedIn() throws Exception {
        HttpSession session=mock(HttpSession.class);
        PowerMockito.mockStatic(Response.class);
        Response response=mock(Response.class);
        Response.ResponseBuilder responseBuilder=mock(Response.ResponseBuilder.class);

        when(Response.seeOther(isA(URI.class))).thenReturn(responseBuilder);
        when(responseBuilder.build()).thenReturn(response);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        Response response1=voteEndPoint.voting("VOTE_UP",10l);
        assertEquals(response1,response);
        verifyZeroInteractions(voteService);
    }

    @Test
    public void shouldReturnResponseClassAndUpdateVoteWhenUserIsLoggedIn() throws Exception {
        long userId=10;
        long discoveryId=20;
        User user=new User();
        user.setId(10);
        HttpSession session=mock(HttpSession.class);
        PowerMockito.mockStatic(Response.class);
        Response response=mock(Response.class);
        Response.ResponseBuilder responseBuilder=mock(Response.ResponseBuilder.class);

        when(Response.seeOther(isA(URI.class))).thenReturn(responseBuilder);
        when(responseBuilder.build()).thenReturn(response);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Response response1=voteEndPoint.voting("VOTE_UP",discoveryId);
        verify(voteService).updateVote(userId,discoveryId,VoteType.VOTE_UP);
        assertEquals(response,response1);
    }

}