package pl.przemek.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.przemek.model.Role;
import pl.przemek.model.User;
@Stateless
public class JpaRoleRepositoryImpl implements JpaRoleRepository {

	 @PersistenceContext
	private EntityManager em;
	
public void update(Role role,User user){
	role.getUsers().add(user);
	em.merge(role);
}
public Role get(String name_role){
	TypedQuery<Role> getRole = em.createNamedQuery("Role.findByName", Role.class);
	getRole.setParameter("role_name", name_role);
	List<Role> getroles=getRole.getResultList();
	Role chosenRole=getroles.get(0);
	return chosenRole;
	
}

}
