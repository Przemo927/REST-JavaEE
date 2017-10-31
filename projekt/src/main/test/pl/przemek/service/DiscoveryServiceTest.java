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
import java.util.*;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.*;
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
        verify(discRepo).add(discovery);
    }


    @Test
    public void shouldThrowNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Comment is null");
        discoveryService.addDiscovery(null);

    }

    @Test
    public void shouldExecuteGetMethodOfDiscoveryRepository() throws Exception {
        discoveryService.getById(anyLong());
        verify(discRepo).get(anyLong());

    }

    @Test
    public void shouldUseTheSameLong() throws Exception {
        long id=12345;
        discoveryService.getById(id);
        verify(discRepo).get(id);
        verify(discRepo,never()).get(not(eq(id)));
    }

    @Test
    public void shouldExecuteGetByNameMethodOfDiscoveryRepository() throws Exception {
        discoveryService.getByName(anyString());
        verify(discRepo).getByName(anyString());
    }

    @Test
    public void shouldUseTheSameString() throws Exception {
        String name="Name";
        discoveryService.getByName(name);
        verify(discRepo,never()).getByName(not(eq(name)));
    }

    public Object[] validNamesOfComparators() {
        return $("time", "popular");
    }

    @Test
    @Parameters(method ="validNamesOfComparators")
    public void ShouldExecuteGetMethodWithValidComparator(String names) throws Exception {
        discoveryService.getAll(names);
        verify(discRepo).getAll(isA(Comparator.class));
    }

    public Object[] invalidNamesOfComparators() {
        return $( "date","comparator","Time","Popular","TIME","POPULAR",null);
    }

    @Test
    @Parameters(method ="invalidNamesOfComparators")
    public void ShouldExecuteMethodWithoutComparatorWhenNameOfComparatorIsInvalid(String names) throws Exception{
        discoveryService.getAll(names);
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

    @Test
    public void methodsShouldUseTheSameValueOfId() throws Exception {
        long id=12345;
        discoveryService.removeDiscoveryById(id);
        verify(discRepo,never()).get(not(eq(id)));
        verify(voteRepo,never()).removeByDiscoveryId(not(eq(id)));

    }
    @Test
    public void shouldExecuteMethodUpdate() throws Exception {
        Discovery discovery=mock(Discovery.class);
        discoveryService.updateDiscovery(discovery);
        verify(discRepo).update(discovery);

    }
}