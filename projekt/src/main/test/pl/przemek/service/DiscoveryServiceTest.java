package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaVoteRepository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
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
    public void shouldExecuteGetMethodOfDiscoveryRepository() throws Exception {
        discoveryService.getById(anyLong());
        verify(discRepo).get(anyLong());
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
    public void shouldDoNothingAndReturnNullIfNameIsNull(){
        discoveryService.getByName(null);
        verify(discRepo,never()).getByName(null);
        verify(discRepo,never()).getByName(anyString());
        assertEquals(null,discoveryService.getByName(null));
    }

    @Test
    public void shouldDoNothingAndReturnNullIfNameIsEmptyWord() throws Exception {
        String name="";
        discoveryService.getByName(name);
        verify(discRepo,never()).getByName(null);
        verify(discRepo,never()).getByName(name);
        verify(discRepo,never()).getByName(anyString());
        assertEquals(null,discoveryService.getByName(name));
    }

    public Object[] discoveryId(){
        return $(1,1000,123456,987654321);
    }

    @Test
    @Parameters(method = "discoveryId")
    public void shouldUseTheSameLong(long id) throws Exception {
        discoveryService.getById(id);
        verify(discRepo,times(1)).get(id);
        verify(discRepo,never()).get(not(eq(id)));
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
        verify(discRepo).getAll();
    }

    @Test
    public void shouldExecuteGetAllMethodWithTimeComparator() throws Exception{
        discoveryService.getAll("time");
        verify(discRepo).getAll(isA(DiscoveryService.TimeComparator.class));
    }
    @Test
    public void shouldExecuteGetAllMethodWithPopularComparator() throws Exception{
        discoveryService.getAll("popular");
        verify(discRepo).getAll(isA(DiscoveryService.PopularComparator.class));
    }

    @Test
    public void shouldReturnListWithTheSameOrderAfterSortByTime() throws Exception{
        Discovery discovery1=new Discovery();
        discovery1.setTimestamp(Timestamp.valueOf("2001-01-01 00:00:00"));
        Discovery discovery2=new Discovery();
        discovery2.setTimestamp(Timestamp.valueOf("2002-01-01 00:00:00"));
        Discovery discovery3=new Discovery();
        discovery3.setTimestamp(Timestamp.valueOf("2003-01-01 00:00:00"));
        List<Discovery> listWithVelidOrder= Arrays.asList(discovery1,discovery2,discovery3);
        List<Discovery> listOrderdByTimeCompartor= Arrays.asList(discovery3,discovery1,discovery2);

        assertTrue(!(listOrderdByTimeCompartor.equals(listWithVelidOrder)));
        listOrderdByTimeCompartor.sort(new DiscoveryService.TimeComparator());
        assertTrue(listOrderdByTimeCompartor.equals(listWithVelidOrder));


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
    public void shouldExecuteMethodGetRemoveByDiscoveryIdAndRemove() throws Exception {
        Discovery discovery=mock(Discovery.class);

        when(discRepo.get(anyLong())).thenReturn(discovery);
        discoveryService.removeDiscoveryById(anyLong());

        verify(voteRepo).removeByDiscoveryId(anyLong());
        verify(discRepo).remove(discovery);

    }

    public Object[] discoveryIdRemove(){
        return $(12345,1,0,987654321);
    }

    @Test
    @Parameters(method = "discoveryIdRemove")
    public void methodsShouldUseTheSameValueOfId(long id) throws Exception {
        discoveryService.removeDiscoveryById(id);
        verify(discRepo,never()).remove(isA(Discovery.class));
        verify(discRepo,never()).get(not(eq(id)));
        verify(voteRepo,never()).removeByDiscoveryId(not(eq(id)));
        verify(discRepo,times(1)).get(eq(id));
        verify(voteRepo,times(1)).removeByDiscoveryId(eq(id));
    }
    @Test
    public void shouldExecuteMethodUpdate() throws Exception {
        Discovery discovery=mock(Discovery.class);
        discoveryService.updateDiscovery(discovery);
        verify(discRepo).update(discovery);

    }
}