package pl.przemek.repository;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import pl.przemek.model.User;

@Stateless
public class UserRepository {
	
@PersistenceContext
private EntityManager em;


public void add(User user) {
	try{
    em.persist(user);
	}
	catch (EJBException | PersistenceException e) {
		 
	}
	
 }


public void remove(User user) {
    em.remove(user);
}


public User update(User user) {
	User updateUser=em.merge(user);
	return updateUser;
}


public User get(Long id) {
    User user = em.find(User.class, id);
    return user;
}
 
public List<User> getAll() {
	TypedQuery<User> getAllQuery = em.createNamedQuery("User.findAll", User.class);
    List<User> users = getAllQuery.getResultList();
    return users;
}
public User getUserByUsername(String username) {
	TypedQuery<User> getAllQuery = em.createNamedQuery("User.findByUsername", User.class);
	getAllQuery.setParameter("username",username);
    List<User> users = getAllQuery.getResultList();
    User chosenuser=users.get(0);
    return chosenuser;
	
}
}
