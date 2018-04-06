package pl.przemek.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.przemek.model.VoteComment;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JpaVoteCommentRepositoryImplTest {

    @Mock
    private EntityManager em;
    @InjectMocks
    private JpaVoteCommentRepositoryImpl jpaVoteCommentRepository;

    @Test
    public void shouldReturnNullWhenListOfCommentsVoteIsNotExist() throws Exception {
        TypedQuery<VoteComment> query=mock(TypedQuery.class);
        List<VoteComment> voteCommentList=new ArrayList<>();

        when(em.createNamedQuery(anyString(),eq(VoteComment.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteCommentList);

        assertEquals(null,jpaVoteCommentRepository.getVoteByUserIdCommentId(anyLong(),anyLong()));
    }

    @Test
    public void shouldReturnCommentVoteWhenCommentsVoteIsExist() throws Exception{
        VoteComment vote1=mock(VoteComment.class);
        VoteComment vote2=mock(VoteComment.class);
        TypedQuery<VoteComment> query=mock(TypedQuery.class);
        List<VoteComment> voteCommentList=new ArrayList<>();
        voteCommentList.add(vote1);
        voteCommentList.add(vote2);

        when(em.createNamedQuery(anyString(),eq(VoteComment.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteCommentList);

        assertNotEquals(null,jpaVoteCommentRepository.getVoteByUserIdCommentId(anyLong(),anyLong()));
        assertEquals(vote1,jpaVoteCommentRepository.getVoteByUserIdCommentId(anyLong(),anyLong()));
        assertNotEquals(vote2,jpaVoteCommentRepository.getVoteByUserIdCommentId(anyLong(),anyLong()));
    }
}