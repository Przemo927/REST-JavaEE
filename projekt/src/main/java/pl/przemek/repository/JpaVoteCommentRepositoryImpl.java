package pl.przemek.repository;

import pl.przemek.model.VoteComment;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaVoteCommentRepositoryImpl extends JpaRepository<VoteComment> implements JpaVoteRepository<VoteComment> {

    @Override
    public List<VoteComment> getVoteByUserIdVotedElementId(long UserId, long CommentId) {
        TypedQuery<VoteComment> query=em.createNamedQuery("VoteComment.findByUserIdCommentId",VoteComment.class);
        query.setParameter("userId",UserId);
        query.setParameter("commentId",CommentId);
        return query.getResultList();
    }

    @Override
    public void removeByVotedElementId(long CommentId) {

    }
}
