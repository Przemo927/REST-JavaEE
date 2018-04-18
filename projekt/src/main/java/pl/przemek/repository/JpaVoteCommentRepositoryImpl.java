package pl.przemek.repository;

import pl.przemek.model.VoteComment;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaVoteCommentRepositoryImpl extends JpaRepository<VoteComment> implements JpaVoteCommentRepository {

    @Override
    public VoteComment getVoteByUserIdCommentId(Long UserId, Long CommentId) {
        TypedQuery<VoteComment> query=em.createNamedQuery("VoteComment.findByUserIdCommentId",VoteComment.class);
        query.setParameter("userId",UserId);
        query.setParameter("commentId",CommentId);
        VoteComment voteComment = null;
        List<VoteComment> listOfVoteComment = query.getResultList();
        if(listOfVoteComment.isEmpty()){
            return null;
        }
        else {
            return listOfVoteComment.get(0);
        }
    }

    @Override
    public void removeByCommentId(Long CommentId) {

    }
}
