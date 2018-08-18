package pl.przemek.repository;

import pl.przemek.model.Role;
import pl.przemek.model.User;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
@Stateless
public class JpaRoleRepositoryImpl extends JpaRepository<Role> implements JpaRoleRepository {

public void update(Role role,User user){
	role.getUsers().add(user);
	em.merge(role);
}
public List<Role> getRoles(String nameRole){
	TypedQuery<Role> getRole = em.createNamedQuery("Role.findByName", Role.class);
	getRole.setParameter("role_name", nameRole);
	return getRole.getResultList();
	
}

}
