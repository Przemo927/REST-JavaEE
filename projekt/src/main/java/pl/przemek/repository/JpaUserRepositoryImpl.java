package pl.przemek.repository;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.*;

import pl.przemek.model.User;

@Stateless
public class JpaUserRepositoryImpl implements JpaUserRepository  {
	
@PersistenceContext
private EntityManager em;

@PermitAll
public void add(User user) {
    em.persist(user);
	}

@RolesAllowed("admin")
public void remove(User user) {
    em.remove(user);
}

@RolesAllowed("admin")
public User update(User user) {
	User updateUser=em.merge(user);
	return updateUser;
}

    @RolesAllowed({"admin","user"})
public User get(Long id) {
    User user = em.find(User.class, id);
    return user;
}
@RolesAllowed({"admin","user"})
public List<User> getAll() {
	TypedQuery<User> getAllQuery = em.createNamedQuery("User.findAll", User.class);
    List<User> users = getAllQuery.getResultList();
    return users;
}
@RolesAllowed({"admin","user"})
public User getUserByUsername(String username) {
	TypedQuery<User> getAllQuery = em.createNamedQuery("User.findByUsername", User.class);
	getAllQuery.setParameter("username",username);
    List<User> users = getAllQuery.getResultList();
    User chosenuser=users.get(0);
    return chosenuser;
	
}
@PermitAll
public boolean checkPresenceOfUserByUsername(String username){
    Query query=em.createNativeQuery("SELECT 1 FROM User WHERE username=:username");
    query.setParameter("username",username);
    List list=query.getResultList();
    if(list.size()!=0){
        return false;
    }
    return true;
}
    @PermitAll
    public boolean checkPresenceOfEmail(String email){
        Query query=em.createNativeQuery("SELECT 1 FROM User WHERE email=:email ");
        query.setParameter("email",email);
        List list=query.getResultList();
        if(list.size()!=0){
            return false;
        }
        return true;
    }
}
