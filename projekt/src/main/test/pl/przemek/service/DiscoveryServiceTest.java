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
import pl.przemek.model.Vote;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaVoteRepository;
import pl.przemek.repository.inMemoryRepository.JpaCommentRepositoryInMemoryImpl;
import pl.przemek.repository.inMemoryRepository.JpaDiscoveryRepositoryInMemoryImpl;
import pl.przemek.repository.inMemoryRepository.JpaVoteDicoveryRepositoryInMemoryImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class DiscoveryServiceTest {

    private JpaDiscoveryRepository discRepo;
    private DiscoveryService discoveryService;
    private JpaVoteRepository voteRepo;
    private JpaCommentRepository commentRepo;
    private Logger logger=Logger.getLogger(this.getClass().getName());

    @Before
    public void setUp() throws Exception {
        discRepo=mock(JpaDiscoveryRepository.class);
        voteRepo=mock(JpaVoteRepository.class);
        logger=mock(Logger.class);
        commentRepo=mock(JpaCommentRepository.class);
        discoveryService=new DiscoveryService(logger,discRepo,voteRepo,commentRepo);
    }

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    private void createDiscoveryServiceObjectWithInMemoryLayer(){
        discRepo=new JpaDiscoveryRepositoryInMemoryImpl();
        voteRepo=new JpaVoteDicoveryRepositoryInMemoryImpl();
        commentRepo=new JpaCommentRepositoryInMemoryImpl();
        discoveryService=new DiscoveryService(logger,discRepo,voteRepo,commentRepo);
    }

    @Test
    public void shouldDoNotAddDiscoveryWhenIsNull(){
        createDiscoveryServiceObjectWithInMemoryLayer();
        int amountOfDiscoveriesBeforeAdd=discRepo.getAll("",Discovery.class).size();

        discoveryService.addDiscovery(null);

        assertEquals(discRepo.getAll("", Discovery.class).size(), amountOfDiscoveriesBeforeAdd);
    }
    @Test
    public void shouldAddDiscoveryWhenIsNotNull(){
        createDiscoveryServiceObjectWithInMemoryLayer();
        long id=1234;
        Discovery discovery=new Discovery();
        discovery.setId(id);
        int amountOfDiscoveryBeforeAdd=discRepo.getAll("",Discovery.class).size();

        discoveryService.addDiscovery(discovery);
        Discovery addedDiscovery=discRepo.get(Discovery.class,id);

        assertEquals(discRepo.getAll("", Discovery.class).size(), amountOfDiscoveryBeforeAdd + 1);
        assertNotNull(addedDiscovery);
        assertSame(addedDiscovery, discovery);
        assertEquals(0, addedDiscovery.getUpVote());
        assertEquals(0, addedDiscovery.getDownVote());
        assertNotNull(addedDiscovery.getTimestamp());
    }
    @Test
    public void shouldExecuteMethodAddOfDiscoveryRepository() throws Exception {
        Discovery discovery=mock(Discovery.class);
        discoveryService.addDiscovery(discovery);
        verify(discovery,times(1)).setDownVote(0);
        verify(discovery,times(1)).setUpVote(0);
        verify(discovery,times(1)).setTimestamp(isA(Timestamp.class));
        verify(discRepo,times(1)).add(discovery);
    }


    @Test
    public void shouldDoNothingWhenDiscoveryIsNull() throws Exception {
        discoveryService.addDiscovery(null);
        verify(discRepo,never()).add(null);
        verify(discRepo,never()).add(isA(Discovery.class));
    }

    @Test
    public void shouldExecuteGetWithLimitOfDiscoveryRepository() throws Exception {
        createDiscoveryServiceObjectWithInMemoryLayer();
        List<Discovery> listOfAllDiscoveries=discRepo.getAll("",Discovery.class);
        int begin=listOfAllDiscoveries.size()-8;
        int quantity=5;

        assertEquals(listOfAllDiscoveries.subList(begin, quantity), discoveryService.getWithLimit(begin, quantity));
    }

    @Test
    public void shouldExecuteGetMethodOfDiscoveryRepository() throws Exception {
        discoveryService.getById(1);
        verify(discRepo,times(1)).get(eq(Discovery.class),anyLong());
    }

    @Test
    public void shouldDoNotExecuteGetByNameIfNameIsNull(){
        discoveryService.getByName(null);
        verify(discRepo,never()).getByName(null);
        verify(discRepo,never()).getByName(anyString());
    }

    @Test
    public void shouldDoNotExecuteGetByNameIfNameIsEmptyWord() throws Exception {
        String name="";
        discoveryService.getByName(name);
        verify(discRepo,never()).getByName(null);
        verify(discRepo,never()).getByName(name);
        verify(discRepo,never()).getByName(anyString());
    }

    public Object[] discoveryName(){
        return $("name","null","NAME","name name");
    }
    @Test
    @Parameters(method = "discoveryName")
    public void shouldReturnDiscoveryIfNameIsNotNullAndNotEmptyWord(String name){
        createDiscoveryServiceObjectWithInMemoryLayer();
        Discovery discovery=new Discovery();
        discovery.setName(name);
        discRepo.add(discovery);

        assertEquals(discovery,discoveryService.getByName(name).get(0));
    }

    public Object[] validNamesOfComparators() {
        return $("time", "popular");
    }

    @Test
    @Parameters(method ="validNamesOfComparators")
    public void ShouldExecuteGetMethodWithValidComparator(String names) throws Exception {
        discoveryService.getAll(names);
        verify(discRepo,times(1)).getAll(isA(Comparator.class));
    }

    public Object[] invalidNamesOfComparators() {
        return $( "date","comparator","Time","Popular","TIME","POPULAR");
    }
    @Test
    @Parameters(method ="invalidNamesOfComparators")
    public void ShouldExecuteMethodWithoutComparatorWhenNameOfComparatorIsInvalid(String names) throws Exception{
        discoveryService.getAll(names);
        verify(discRepo,never()).getAll(isA(Comparator.class));
        verify(discRepo).getAll(anyString(),eq(Discovery.class));
    }

    @Test
    public void shouldExecuteGetAllMethodWithTimeComparator() throws Exception{
        createDiscoveryServiceObjectWithInMemoryLayer();

        List<Discovery> listOfDiscoveries=discRepo.getAll("",Discovery.class);
        listOfDiscoveries.sort(new DiscoveryService.TimeComparator());

        assertEquals(listOfDiscoveries,discoveryService.getAll("time"));
    }
    @Test
    public void shouldExecuteGetAllMethodWithPopularComparator() throws Exception{
        discoveryService.getAll("popular");
        List<Discovery> listOfDiscoveries=discRepo.getAll("",Discovery.class);
        listOfDiscoveries.sort(new DiscoveryService.PopularComparator());

        assertEquals(listOfDiscoveries,discoveryService.getAll("popular"));
    }

    @Test
    public void shouldReturnListWithTheSameOrderAfterSortByTime() throws Exception{
        Discovery discovery1=new Discovery();
        discovery1.setTimestamp(Timestamp.valueOf("2001-01-01 08:00:00"));
        Discovery discovery2=new Discovery();
        discovery2.setTimestamp(Timestamp.valueOf("2001-01-01 09:00:00"));
        Discovery discovery3=new Discovery();
        discovery3.setTimestamp(Timestamp.valueOf("2001-01-01 10:00:00"));
        List<Discovery> listWithValidOrder= Arrays.asList(discovery1,discovery2,discovery3);
        List<Discovery> listOrderdByTimeCompartor= Arrays.asList(discovery3,discovery1,discovery2);

        assertTrue(!(listOrderdByTimeCompartor.equals(listWithValidOrder)));
        listOrderdByTimeCompartor.sort(new DiscoveryService.TimeComparator());
        assertEquals(listOrderdByTimeCompartor, listWithValidOrder);


    }

    @Test
    public void shouldReturnListWithTheSameOrderAfterSortByPopular() throws Exception{
        Discovery discovery1=new Discovery();
        discovery1.setUpVote(10);
        discovery1.setDownVote(1);
        Discovery discovery2=new Discovery();
        discovery2.setUpVote(5);
        discovery2.setDownVote(5);
        Discovery discovery3=new Discovery();
        discovery3.setUpVote(1);
        discovery3.setDownVote(10);
        List<Discovery> listWithVelidOrder= Arrays.asList(discovery1,discovery2,discovery3);
        List<Discovery> listOrderedByPopularComparator= Arrays.asList(discovery3,discovery1,discovery2);

        assertTrue(!(listOrderedByPopularComparator.equals(listWithVelidOrder)));
        listOrderedByPopularComparator.sort(new DiscoveryService.PopularComparator());
        assertEquals(listOrderedByPopularComparator, listWithVelidOrder);

    }

    @Test
    public void shouldRemoveDiscoveryAndVote() throws Exception {
        createDiscoveryServiceObjectWithInMemoryLayer();

        Discovery discovery=new Discovery();
        discovery.setId(1234);
        discRepo.add(discovery);
        int amountOfdiscoveriesBeforeRemove=discRepo.getAll("",Discovery.class).size();

        Vote vote= new Vote();
        vote.setId(4321);
        vote.setDiscovery(discovery);
        voteRepo.add(vote);
        int amountOfVotesBeforeRemove=voteRepo.getAll("",Vote.class).size();

        Comment comment=new Comment();
        comment.setDiscovery(discovery);
        comment.setId(5678);
        commentRepo.add(comment);
        int amountOfCommentsBeforeRemove=commentRepo.getAll(anyString(),Comment.class).size();

        assertEquals(discovery,discRepo.get(Discovery.class,1234));
        assertEquals(vote,voteRepo.get(Vote.class,4321));
        assertEquals(comment,commentRepo.get(Comment.class,5678));

        discoveryService.removeDiscoveryById(1234);

        assertEquals(amountOfdiscoveriesBeforeRemove-1,discRepo.getAll(anyString(),Discovery.class).size());
        assertNull(discRepo.get(Discovery.class, 1234));
        assertEquals(amountOfVotesBeforeRemove-1,voteRepo.getAll(anyString(),Vote.class).size());
        assertNull(voteRepo.get(Vote.class, 4321));
        assertEquals(amountOfCommentsBeforeRemove-1,commentRepo.getAll(anyString(),Comment.class).size());
        assertNull(commentRepo.get(Comment.class,5678));

    }
}