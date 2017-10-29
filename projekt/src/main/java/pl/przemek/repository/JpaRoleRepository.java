package pl.przemek.repository;

import pl.przemek.model.Role;
import pl.przemek.model.User;

import java.util.List;

public interface JpaRoleRepository {
    public void update(Role role, User user);
    public List<Role> getRoles(String name_role);
}
