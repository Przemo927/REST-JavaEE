package pl.przemek.repository;

import pl.przemek.model.VoteComment;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Null;

@Stateless
public class JpaVoteCommentRepositoryImpl implements JpaVoteCommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(VoteComment vote) {
        em.persist(vote);
    }

    @Override
    public VoteComment update(VoteComment vote) {
        return em.merge(vote);
    }

    @Override
    public void remove(VoteComment vote) {
        em.remove(em.merge(vote));

    }

    @Override
    public VoteComment get(Long id) {
        return em.find(VoteComment.class,id);
    }

    @Override
    public VoteComment getVoteByUserIdCommentId(Long UserId, Long CommentId) {
        TypedQuery<VoteComment> query=em.createNamedQuery("VoteComment.findByUserIdCommentId",VoteComment.class);
        query.setParameter("userId",UserId);
        query.setParameter("commentId",CommentId);
        VoteComment voteComment = null;
        try {
            voteComment = query.getSingleResult();
        } catch (NoResultException nre){
        }
        return voteComment;
    }

    @Override
    public void removeByCommentId(Long CommentId) {

    }
}
