package pl.przemek.repository;


import pl.przemek.model.Comment;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaCommentRepositoryImpl extends JpaRepository<Comment> implements JpaCommentRepository{

    @RolesAllowed({"admin","user"})
    @Override
    public List<Comment> getByDiscoveryName(String name) {
        TypedQuery<Comment> query=em.createNamedQuery("Comment.findByDiscoveryName",Comment.class);
        query.setParameter("name",name);
        return query.getResultList();
    }

    @RolesAllowed({"admin","user"})
    @Override
    public List<Comment> getByDiscoveryId(long id) {
        TypedQuery<Comment> query=em.createNamedQuery("Comment.findByDiscoveryId",Comment.class);
        query.setParameter("id",id);
        return query.getResultList();
    }

}
