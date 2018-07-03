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

@Override
@PermitAll
public void add(User user) {
    em.persist(user);
	}
    private final static String PRESENCE_OF_USERNAME="SELECT 1 FROM User WHERE username=:username";
    private final static String PRESENCE_OF_EMAIL="SELECT 1 FROM User WHERE email=:email";
    @RolesAllowed("admin")
    public Integer updateWithoutPassword(User user) {
        Query queryUpdateUser = em.createNamedQuery("User.editUser");
        queryUpdateUser.setParameter("username", user.getUsername());
        queryUpdateUser.setParameter("email", user.getEmail());
        queryUpdateUser.setParameter("active", user.isActive());
        queryUpdateUser.setParameter("id", user.getId());
        return queryUpdateUser.executeUpdate();
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
        Query query=em.createNativeQuery(PRESENCE_OF_USERNAME);
        query.setParameter("username",username);
        List list=query.getResultList();
        if(list.size()!=0){
            return false;
        }
        return true;
    }
        @PermitAll
    public boolean checkPresenceOfEmail(String email){
            Query query=em.createNativeQuery(PRESENCE_OF_EMAIL);
            query.setParameter("email",email);
            List list=query.getResultList();
            if(list.size()!=0){
                return false;
            }
            return true;
        }
}
