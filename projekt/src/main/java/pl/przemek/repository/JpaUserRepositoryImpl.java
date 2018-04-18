package pl.przemek.repository;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.*;

import pl.przemek.model.User;

@Stateless
public class JpaUserRepositoryImpl extends JpaRepository<User> implements JpaUserRepository  {
	
@PersistenceContext
private EntityManager em;

@Override
@PermitAll
public void add(User user) {
    em.persist(user);
	}


@RolesAllowed("admin")
public Integer updateWithoutPassword(User user) {
    Query queryUpdateUser = em.createNamedQuery("User.editUser");
    queryUpdateUser.setParameter("username", user.getUsername());
    queryUpdateUser.setParameter("email", user.getEmail());
    queryUpdateUser.setParameter("active", user.isActive());
    queryUpdateUser.setParameter("id", user.getId());
    return queryUpdateUser.executeUpdate();
}

    @Override
    public User get(Class<User> clazz, Long id) {
        return null;
    }


    @RolesAllowed({"admin","user"})
public List<User> getUserByUsername(String username) {
	TypedQuery<User> getAllQuery = em.createNamedQuery("User.findByUsername", User.class);
	getAllQuery.setParameter("username",username);
    List<User> users = getAllQuery.getResultList();
    return users;
	
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
