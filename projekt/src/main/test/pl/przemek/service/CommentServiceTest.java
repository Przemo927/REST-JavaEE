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
import pl.przemek.repository.inMemoryRepository.JpaCommentRepositoryInMemoryImpl;
import pl.przemek.repository.inMemoryRepository.JpaDiscoveryRepositoryInMemoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class CommentServiceTest {
    private JpaCommentRepository commentRepo;
    private JpaDiscoveryRepository discRepo;
    private CommentService commentService;
    private Logger logger=Logger.getLogger(this.getClass().getName());

    @Before
    public void setUp(){
        commentRepo=mock(JpaCommentRepository.class);
        discRepo=mock(JpaDiscoveryRepository.class);
        commentService=new CommentService(logger,commentRepo,discRepo);

    }

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    private void createObjectOfCommentServiceWithInMemoryLayer(){
        commentRepo=new JpaCommentRepositoryInMemoryImpl();
        discRepo=new JpaDiscoveryRepositoryInMemoryImpl();
        commentService=new CommentService(logger,commentRepo,discRepo);
    }

    public Object[] commentDiscovery(){
        return $(new Object[]{new Comment(),1234},new Object[]{null,4321},new Object[]{null,1234});
    }

    @Test
    public void shouldExecuteGetAllMethod() throws Exception{
        commentService.getAllComment();
        verify(commentRepo).getAll(anyString(),eq(Comment.class));
    }

    @Test
    public void shouldExecuteGetByDiscoveryIdMethod() throws Exception {
        commentService.getByDiscoveryId(anyLong());
        verify(commentRepo).getByDiscoveryId(anyLong());
    }

    @Test
    public void shouldExecuteGetByDiscoveryNameMethod() throws Exception {
        commentService.getByDiscoveryName(anyString());
        verify(commentRepo).getByDiscoveryName(anyString());
    }
}