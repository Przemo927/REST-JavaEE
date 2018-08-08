package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import pl.przemek.model.Discovery;
import pl.przemek.model.Vote;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaVoteRepository;
import pl.przemek.repository.inMemoryRepository.JpaDiscoveryRepositoryInMemoryImpl;
import pl.przemek.repository.inMemoryRepository.JpaVoteRepositoryInMemoryImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.not;
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
    private Comparator<Discovery> comparator;

    @Before
    public void setUp() throws Exception {
        discRepo=mock(JpaDiscoveryRepository.class);
        voteRepo=mock(JpaVoteRepository.class);
        discoveryService=new DiscoveryService(discRepo,voteRepo);
        comparator=new DiscoveryService.TimeComparator();

    }

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    private void createDiscoveryServiceObjectWithInMemoryLayer(){
        discRepo=new JpaDiscoveryRepositoryInMemoryImpl();
        voteRepo=new JpaVoteRepositoryInMemoryImpl();
        discoveryService=new DiscoveryService(discRepo,voteRepo);
    }

    @Test
    public void shouldDoNotAddDiscoveryWhenIsNull(){
        createDiscoveryServiceObjectWithInMemoryLayer();
        int amountOfDiscoveriesBeforeAdd=discRepo.getAll("",Discovery.class).size();

        discoveryService.addDiscovery(null);

        assertTrue(discRepo.getAll("",Discovery.class).size()==amountOfDiscoveriesBeforeAdd);
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

        assertTrue(discRepo.getAll("",Discovery.class).size()==amountOfDiscoveryBeforeAdd+1);
        assertTrue(addedDiscovery!=null);
        assertTrue(addedDiscovery==discovery);
        assertTrue(addedDiscovery.getUpVote()==0);
        assertTrue(addedDiscovery.getDownVote()==0);
        assertTrue(addedDiscovery.getTimestamp()!=null);
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

    public Object[] beginAndQuantity(){
        return $(new Integer[]{1,2},new Integer[]{11,22},new Integer[]{0,12345});
    }

    @Test
    @Parameters(method="beginAndQuantity")
    public void shouldUseTheSameValues(int begin, int quantity){
        discoveryService.getWithLimit(begin,quantity);
        verify(discRepo,times(1)).getWithLimit(begin,quantity);
        verify(discRepo,never()).getWithLimit(quantity,begin);
        verify(discRepo,never()).getWithLimit(0,0);
    }

    @Test
    public void shouldExecuteGetWithLimitOfDiscoveryRepository() throws Exception {
        createDiscoveryServiceObjectWithInMemoryLayer();
        List<Discovery> listOfAllDiscoveries=discRepo.getAll("",Discovery.class);
        int begin=listOfAllDiscoveries.size()-8;
        int quantity=5;

        assertTrue(listOfAllDiscoveries.subList(begin,quantity).equals(discoveryService.getWithLimit(begin,quantity)));
    }

    @Test
    public void shouldExecuteGetMethodOfDiscoveryRepository() throws Exception {
        discoveryService.getById(1);
        verify(discRepo,times(1)).get(eq(Discovery.class),anyLong());
    }

    public Object[] name(){
        return $("Przemek","01010","null");
    }
    @Test
    @Parameters(method="name")
    public void shouldUseTheSameName(String name){
        discoveryService.getByName(name);
        verify(discRepo,times(1)).getByName(name);
        verify(discRepo,never()).getByName(not(eq(name)));
        verify(discRepo,never()).getByName(null);
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

    public Object[] discoveryId(){
        return $(1,1000,123456,987654321);
    }
    @Test
    @Parameters(method = "discoveryId")
    public void shouldUseTheSameLong(long id) throws Exception {
        discoveryService.getById(id);
        verify(discRepo,times(1)).get(Discovery.class,id);
        verify(discRepo,never()).get(eq(Discovery.class),not(eq(id)));
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
        return $( "date","comparator","Time","Popular","TIME","POPULAR",null);
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
        assertTrue(listOrderdByTimeCompartor.equals(listWithValidOrder));


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
        assertTrue(listOrderedByPopularComparator.equals(listWithVelidOrder));

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

        assertEquals(discovery,discRepo.get(Discovery.class,1234));
        assertEquals(vote,voteRepo.get(Vote.class,4321));

        discoveryService.removeDiscoveryById(1234);

        assertEquals(amountOfdiscoveriesBeforeRemove-1,discRepo.getAll("",Discovery.class).size());
        assertEquals(null,discRepo.get(Discovery.class,1234));
        assertEquals(amountOfVotesBeforeRemove-1,voteRepo.getAll("",Vote.class).size());
        assertEquals(null,voteRepo.get(Vote.class,4321));

    }

    public Object[] discoveryIdRemove(){
        return $(12345,1,0,987654321);
    }

    @Test
    @Parameters(method = "discoveryIdRemove")
    public void methodsShouldUseTheSameValueOfId(long id) throws Exception {
        discoveryService.removeDiscoveryById(id);

        verify(discRepo,times(1)).get(Discovery.class,id);
        verify(voteRepo,times(1)).removeByDiscoveryId(id);
    }

}