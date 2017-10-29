package pl.przemek.repository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.przemek.model.VoteComment;

import javax.enterprise.inject.Typed;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JpaVoteCommentRepositoryImplTest {

    @Mock
    private EntityManager em;
    @InjectMocks
    private JpaVoteCommentRepositoryImpl jpaVoteCommentRepository;

    @Rule
    public ExpectedException thrown=ExpectedException.none();
    @Test
    public void throwNoResultExceptionWhenListOfCommentsVoteIsNotExist() throws Exception {
        TypedQuery<VoteComment> query=mock(TypedQuery.class);
        List<VoteComment> voteCommentList=new ArrayList<>();

        when(em.createNamedQuery(anyString(),eq(VoteComment.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(voteCommentList);
        thrown.expect(NoResultException.class);
        thrown.expectMessage("Vote of comment is not exist");
        jpaVoteCommentRepository.getVoteByUserIdCommentId(anyLong(),anyLong());
    }

}