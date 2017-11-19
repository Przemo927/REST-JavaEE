package pl.przemek.rest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.przemek.model.User;
import pl.przemek.model.VoteType;
import pl.przemek.service.VoteCommentService;

import javax.jms.Session;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class VoteCommentEndPointTest {

    private VoteCommentEndPoint voteCommentEndPoint;
    private VoteCommentService voteCommentService;
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        voteCommentService=mock(VoteCommentService.class);
        request=mock(HttpServletRequest.class);
        voteCommentEndPoint=new VoteCommentEndPoint(voteCommentService,request);

    }

    @Test
    public void shouldDoNothingWhenUserIsNotLoggedInAndNotSaveOnSession() throws Exception {
        HttpSession session=mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        voteCommentEndPoint.voting("VOTE_UP",1);

        verifyZeroInteractions(voteCommentService);

    }

    public Object[] voteType() {
        return $(new Object[]{VoteType.VOTE_UP,"VOTE_UP"} , new Object[]{VoteType.VOTE_DOWN,"VOTE_DOWN"}
        );
    }

    @Parameters(method ="voteType")
    @Test
    public void shouldUpdateVoteWhenUserIsLoggedInAndSaveOnSession(VoteType voteType,String voteTypeAsString){
        HttpSession session=mock(HttpSession.class);
        User user=new User();
        long userId=10;
        long commentId=20;
        user.setId(userId);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        voteCommentEndPoint.voting(voteTypeAsString,commentId);
        verify(voteCommentService).updateVote(userId,commentId,voteType);

    }

}