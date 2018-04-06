package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import pl.przemek.model.Comment;
import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
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
        Discovery discovery=mock(Discovery.class);
        when(discRepo.get(anyLong())).thenReturn(discovery);
        commentService.addComment(comment,anyLong());

        verify(commentRepo).add(comment);
    }

    public Object[] commentDiscovery(){
        return $(new Object[]{mock(Comment.class),null},new Object[]{null,mock(Discovery.class)},new Object[]{null,null});
    }
    @Test
    @Parameters(method = "commentDiscovery")
    public void shouldNotAddCommentWhenCommentOrDiscoveryIsNull(Comment comment,Discovery discovery) throws Exception {
        when(discRepo.get(anyLong())).thenReturn(discovery);
        commentService.addComment(comment,anyLong());
        verify(commentRepo,never()).add(comment);
    }

    @Test
    @Parameters(method = "discoveryId")
    public void shouldUseTheSameValueOfLong(long id) throws Exception{
        Comment comment=mock(Comment.class);

        commentService.addComment(comment,id);

        verify(discRepo).get(id);
        verify(discRepo,never()).get(not(eq(id)));
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
    public void shouldDoNothingWhenCommentIsNull() throws Exception{
        commentService.addComment(null,1L);
        verify(commentRepo,never()).add(isA(Comment.class));
    }
    @Test
    public void shouldDoNothinWhenDiscoveryWasNotFound() throws Exception {
        Comment comment=mock(Comment.class);

        when(discRepo.get(anyLong())).thenReturn(null);
        commentService.addComment(comment,1L);

        verify(commentRepo,never()).add(isA(Comment.class));
    }
    @Test
    public void shouldDoNothinWhenDiscoveryWasNotFoundAndCommentIsNull() throws Exception {
        when(discRepo.get(anyLong())).thenReturn(null);

        commentService.addComment(null,1L);

        verify(commentRepo,never()).add(isA(Comment.class));
    }

    @Test
    public void shouldExecuteGetAllMethod() throws Exception{
        commentService.getAllComment();
        verify(commentRepo).getAll();
    }

    public Object[] validNames() {
        return $("time", "popular","AAAAAAAA","123456789");
    }

    @Parameters(method ="validNames")
    @Test
    public void shouldUseTheSameName(String name){
        commentService.getByDiscoveryName(name);
        verify(commentRepo,times(1)).getByDiscoveryName(name);
    }

    @Test
    public void shouldExecuteGetByDiscoveryIdMethod() throws Exception {
        commentService.getByDiscoveryId(anyLong());
        verify(commentRepo).getByDiscoveryId(anyLong());
    }

    @Test
    public void shoulUseTheSameValueOfId() throws Exception {
        long id=12345;

        commentService.getByDiscoveryId(id);

        verify(commentRepo).getByDiscoveryId(id);
        verify(commentRepo,never()).getByDiscoveryId(not(eq(id)));

    }

    @Test
    public void shouldExecuteGetByDiscoveryNameMethod() throws Exception {
        commentService.getByDiscoveryName(anyString());
        verify(commentRepo).getByDiscoveryName(anyString());
    }

    public Object[] discoveryId() {
        return $(1, 0,123456,987654321);
    }

    @Parameters(method ="discoveryId")
    @Test
    public void shouldUseTheSameVaulueOfLong(long id) throws Exception {
        commentService.getByDiscoveryId(id);
        verify(commentRepo,times(1)).getByDiscoveryId(id);
        verify(commentRepo,never()).getByDiscoveryId(not(eq(id)));

    }


}