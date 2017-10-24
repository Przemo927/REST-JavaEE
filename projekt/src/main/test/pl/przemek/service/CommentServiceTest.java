package pl.przemek.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.przemek.model.Comment;
import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class CommentServiceTest {
    private JpaCommentRepository commentRepo;
    private JpaDiscoveryRepository discRepo;
    private CommentService commentService;

    @Before
    public void setUp(){
        commentRepo=mock(JpaCommentRepository.class);
        discRepo=mock(JpaDiscoveryRepository.class);
        commentService=new CommentService(commentRepo,discRepo);

    }

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Test
    public void shouldExecuteAddMethodOfCommentRepo() throws Exception {

        Comment comment=mock(Comment.class);
        commentService.addComment(comment,anyLong());

        verify(commentRepo).add(comment);
    }

    @Test
    public void shouldUseTheSameValueOfLong() throws Exception{
        Comment comment=mock(Comment.class);
        long id=12345;
        commentService.addComment(comment,id);
        verify(discRepo).get(id);
        verify(discRepo,never()).get(1234);
        verify(discRepo,never()).get(12346);
        verify(discRepo,never()).get(1);
    }

    @Test
    public void commentShouldHasAssignDiscovery() throws Exception{
        Comment comment=new Comment();
        Discovery discovery=mock(Discovery.class);
        when(discRepo.get(anyLong())).thenReturn(discovery);
        commentService.addComment(comment,anyLong());
        assertEquals(discovery,comment.getDiscvovery());

    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCommentIsNull() throws Exception{
        thrown.expect(NullPointerException.class);
        commentService.addComment(null,anyLong());
    }
    @Test
    public void shouldThrowNullPointerExceptionWhenIdIsNullAddCommentMethod() throws Exception{
        thrown.expect(NullPointerException.class);
        Comment comment=mock(Comment.class);
        Long id=null;
        commentService.addComment(comment,id);
    }

    @Test
    public void shouldExecuteGetAllMethod() throws Exception{
        commentService.getAllComment();
        verify(commentRepo).getAll();
    }
    @Test
    public void shouldReturnEmptyListOfComments() throws Exception{
        List<Comment> listofComments=new ArrayList<>();
        commentService.getAllComment();
        assertEquals(listofComments,commentRepo.getAll());
    }
    @Test
    public void shouldExecuteGetByDiscoveryIdMethod() throws Exception {
        commentService.getByDiscoveryId(anyLong());
        verify(commentRepo).getByDiscoveryId(anyLong());
    }

    @Test
    public void shouldReturnEmptyListOfCommentsFoundByName() throws Exception {
        List<Comment> listOfComments=new ArrayList<>();
        assertEquals(listOfComments,commentService.getByDiscoveryName(anyString()));
    }
    @Test
    public void shouldUseTheSameString() throws Exception {
        String discoveryName="Name";
        commentService.getByDiscoveryName(discoveryName);
        verify(commentRepo).getByDiscoveryName(discoveryName);
        verify(commentRepo,never()).getByDiscoveryName("Name1");
        verify(commentRepo,never()).getByDiscoveryName(null);
        verify(commentRepo,never()).getByDiscoveryName("name");

    }

    @Test
    public void shouldExecuteGetByDiscoveryNameMethod() throws Exception {
        commentService.getByDiscoveryName(anyString());
        verify(commentRepo).getByDiscoveryName(anyString());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenIdIsNullGetDiscoveryByIdMethod() throws Exception {
        Long id=null;
        thrown.expect(NullPointerException.class);
        commentService.getByDiscoveryId(id);

    }

    @Test
    public void shouldReturnEmptyListOFCommentFoundById() throws Exception {
        List<Comment> listOfComments=new ArrayList<>();
        assertEquals(listOfComments,commentService.getByDiscoveryId(anyLong()));
    }
    @Test
    public void shouldUseTheSameVaulueOfLong() throws Exception {
        long id=12345;
        commentService.getByDiscoveryId(id);
        verify(commentRepo).getByDiscoveryId(id);
        verify(commentRepo,never()).getByDiscoveryId(1);
        verify(commentRepo,never()).getByDiscoveryId(1234);
        verify(commentRepo,never()).getByDiscoveryId(12346);

    }


}