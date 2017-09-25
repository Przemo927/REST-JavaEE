package pl.przemek.repository;


import pl.przemek.model.Comment;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaCommentRepositoryImpl implements JpaCommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(Comment commment) {
        em.persist(commment);
    }

    @Override
    public void remove(Comment comment) {
    em.remove(em.merge(comment));
    }

    @Override
    public Comment update(Comment comment) {
        Comment updatedComment=em.merge(comment);
        return updatedComment;
    }

    @Override
    public List<Comment> getAll() {
        TypedQuery<Comment> query=em.createNamedQuery("Comment.findAll",Comment.class);
        return query.getResultList();
    }

    @Override
    public List<Comment> getByDiscoveryName(String name) {
        TypedQuery<Comment> query=em.createNamedQuery("Comment.findByDiscoveryName",Comment.class);
        query.setParameter("name",name);
        return query.getResultList();
    }

    @Override
    public List<Comment> getByDiscoveryId(long id) {
        TypedQuery<Comment> query=em.createNamedQuery("Comment.findByDiscoveryId",Comment.class);
        query.setParameter("id",id);
        return query.getResultList();
    }

    @Override
    public Comment get(long id) {
        return em.find(Comment.class,id);
    }
}
