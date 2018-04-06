package pl.przemek.repository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import pl.przemek.model.Vote;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JpaVoteRepositoryImplTest {

    @Mock
    private EntityManager em;

    @Spy
    @InjectMocks
    private JpaVoteRepositoryImpl jpaVoteRepository;

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Test
    public void shouldReturnNullWhenListWithFoundVotesIsEmapty() throws Exception {
        TypedQuery query=mock(TypedQuery.class);
        List<Vote> voteList=new ArrayList<>();

        when(em.createQuery(anyString(),eq(Vote.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteList);

        assertEquals(null,jpaVoteRepository.getVoteByUserIdDiscoveryId(anyLong(),anyLong()));
    }

    @Test
    public void shouldReturnFirstVoteFromListWithFoundVotes() throws Exception {
        Vote vote1=mock(Vote.class);
        Vote vote2=mock(Vote.class);
        TypedQuery query=mock(TypedQuery.class);
        List<Vote> voteList=new ArrayList<>();
        voteList.add(vote1);
        voteList.add(vote2);

        when(em.createQuery(anyString(),eq(Vote.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteList);

        assertEquals(vote1,jpaVoteRepository.getVoteByUserIdDiscoveryId(anyLong(),anyLong()));
        assertFalse(vote2==jpaVoteRepository.getVoteByUserIdDiscoveryId(anyLong(),anyLong()));
        assertTrue(vote1==jpaVoteRepository.getVoteByUserIdDiscoveryId(anyLong(),anyLong()));
    }

    @Test
    public void shouldRemoveAllVotesFoundByDiscoveryId() throws Exception {
        Vote vote1=mock(Vote.class);
        Vote vote2=mock(Vote.class);
        List<Vote> voteList=new ArrayList<>();
        voteList.add(vote1);
        voteList.add(vote2);
        TypedQuery query=mock(TypedQuery.class);

        when(em.createQuery(anyString(),eq(Vote.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteList);

        jpaVoteRepository.removeByDiscoveryId(1L);

        verify(jpaVoteRepository,times(1)).remove(vote1);
        verify(jpaVoteRepository,times(1)).remove(vote2);
    }

    @Test
    public void shouldDoNothingWhenListWthFoundVotesIsEmpty() throws Exception {
        List<Vote> voteList=new ArrayList<>();
        TypedQuery query=mock(TypedQuery.class);

        when(em.createQuery(anyString(),eq(Vote.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteList);

        jpaVoteRepository.removeByDiscoveryId(1L);

        verify(jpaVoteRepository,never()).remove(isA(Vote.class));
    }

}