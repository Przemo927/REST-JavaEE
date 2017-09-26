package pl.przemek.repository;

import pl.przemek.model.Role;
import pl.przemek.model.User;

public interface JpaRoleRepository {
    public void update(Role role, User user);
    public Role get(String name_role);
}