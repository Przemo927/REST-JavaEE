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

import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertTrue;
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

    private void createObjectOfCommentServiceWithInMemoryLayer(){
        commentRepo=new JpaCommentRepositoryInMemoryImpl();
        discRepo=new JpaDiscoveryRepositoryInMemoryImpl();
        commentService=new CommentService(commentRepo,discRepo);
    }
    @Test
    public void shouldAddCommentWithParentDiscovery() throws Exception {
        createObjectOfCommentServiceWithInMemoryLayer();
        Comment comment=new Comment();
        int amountOfDiscoveriesBeforeAdd=Integer.valueOf(String.valueOf(discRepo.getQuantityOfDiscoveries()));
        Discovery discovery=new Discovery();
        discovery.setId(123);

        discRepo.add(discovery);

        commentService.addComment(comment,123);
        assertTrue(comment.getDiscovery()!=null);
        assertTrue(comment.getDiscovery().getId()==123);
        assertTrue(commentRepo.getAll("",Comment.class).size()==amountOfDiscoveriesBeforeAdd+1);
        assertTrue(commentRepo.getAll("",Comment.class).contains(comment));
    }

    public Object[] commentDiscovery(){
        return $(new Object[]{new Comment(),1234},new Object[]{null,4321},new Object[]{null,1234});
    }
    @Test
    @Parameters(method = "commentDiscovery")
    public void shouldNotAddCommentWhenCommentIsNullOrAndDiscoveryDidNotFind(Comment comment,long discoveryId) throws Exception {
        createObjectOfCommentServiceWithInMemoryLayer();
        ((JpaDiscoveryRepositoryInMemoryImpl)discRepo).setListOfDiscoveries(removeDiscoveryById(1234,discRepo.getAll("",Discovery.class)));
        Discovery disc=new Discovery();
        disc.setId(4321);
        discRepo.add(disc);

        int amountOfComents=commentRepo.getAll(null,null).size();
        commentService.addComment(comment,discoveryId);
        assertTrue(commentRepo.getAll("",Comment.class).size()==amountOfComents);
    }
    private List<Discovery> removeDiscoveryById(long id, List<Discovery> list){
        for(int i=0;i<list.size();i++){
            Discovery disc=list.get(i);
            if(disc.getId()==id)
                list.remove(disc);
        }
        return list;
    }

    @Test
    @Parameters(method = "discoveryId")
    public void shouldUseTheSameValueOfLong(long id) throws Exception{
        Comment comment=mock(Comment.class);

        commentService.addComment(comment,id);

        verify(discRepo).get(Discovery.class,id);
        verify(discRepo,never()).get(eq(Discovery.class),not(eq(id)));
    }

    @Test
    public void shouldExecuteGetAllMethod() throws Exception{
        commentService.getAllComment();
        verify(commentRepo).getAll(anyString(),eq(Comment.class));
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