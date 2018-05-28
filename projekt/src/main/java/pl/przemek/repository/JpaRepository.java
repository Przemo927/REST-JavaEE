package pl.przemek.repository;

import pl.przemek.model.Comment;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class JpaRepository <T> {

    @PersistenceContext
    protected EntityManager em;

    @RolesAllowed({"admin","user"})
    public void add(T element){
        em.persist(element);
    }
    @RolesAllowed({"admin"})
    public void remove(T elemnt) {
        em.remove(em.merge(elemnt));
    }
    @RolesAllowed({"admin","user"})
    public T update(T element) {
        return em.merge(element);
    }
    @RolesAllowed({"admin","user"})
    public T get(Class<T> clazz,long id) {
        return em.find(clazz,id);
    }
    @RolesAllowed({"admin","user"})
    public List<T> getAll(String nameOfQuery,Class<T> clazz) {
        TypedQuery<T> query=em.createNamedQuery(nameOfQuery,clazz);
        return query.getResultList();
    }

}
