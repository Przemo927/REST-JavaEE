package pl.przemek.rest;

import org.junit.Before;
import org.junit.Test;
import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class CommentEndPointTest {

    private CommentService commentService;
    private HttpServletRequest request;
    private CommentEndPoint commentEndPoint;

    @Before
    public void setUp(){
        commentService=mock(CommentService.class);
        request=mock(HttpServletRequest.class);
        commentEndPoint=new CommentEndPoint(commentService,request);
    }
    @Test
    public void shouldAddUserToTheComment() throws Exception {
        long disooveryId=12345;
        User user=mock(User.class);
        HttpSession session=mock(HttpSession.class);
        Comment comment=mock(Comment.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(user);

        commentEndPoint.addComment(comment,disooveryId);
        verify(comment).setUser(user);

    }

    @Test
    public void shouldExecuteMethodAddCommentOfCommentService() throws Exception {
        long disooveryId=12345;
        User user=mock(User.class);
        HttpSession session=mock(HttpSession.class);
        Comment comment=mock(Comment.class);

        when(request.getSession(false)).thenReturn(session);

        commentEndPoint.addComment(comment,disooveryId);
        verify(commentService).addComment(comment,disooveryId);
    }

}