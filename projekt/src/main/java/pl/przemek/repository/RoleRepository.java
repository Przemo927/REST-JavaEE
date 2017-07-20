package pl.przemek.repository;

import pl.przemek.model.Role;
import pl.przemek.model.User;

/**
 * Created by Przemek on 2017-07-19.
 */
public interface RoleRepository {
    public void update(Role role, User user);
    public Role get(String name_role);
}
