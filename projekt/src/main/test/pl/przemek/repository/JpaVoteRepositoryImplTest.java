package pl.przemek.repository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.przemek.model.Vote;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JpaVoteRepositoryImplTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private JpaVoteRepositoryImpl jpaVoteRepository;

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Test
    public void getVoteByUserIdDiscoveryId() throws Exception {
        TypedQuery<Vote> query=mock(TypedQuery.class);
        List<Vote> voteList=new ArrayList<>();

        when(em.createQuery(anyString(),eq(Vote.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteList);
        thrown.expect(NoResultException.class);
        thrown.expectMessage("Vote is not exist");
        jpaVoteRepository.getVoteByUserIdDiscoveryId(anyLong(),anyLong());
    }

    @Test
    public void removeByDiscoveryId() throws Exception {
        List<Vote> voteList=new ArrayList<>();
        voteList.add(new Vote());
        voteList.add(new Vote());
        TypedQuery<Vote> query=mock(TypedQuery.class);
        when(em.createQuery(anyString(),eq(Vote.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteList);
        jpaVoteRepository.removeByDiscoveryId(1L);

    }

}