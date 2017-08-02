package pl.przemek.repository;

import pl.przemek.model.User;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public interface UserRepository {
    public void add(User user);
   // @RolesAllowed("admin")
    public void remove(User user);
   // @RolesAllowed("admin")
    public User update(User user);
    public User get(Long id);
    public List<User> getAll();
    public User getUserByUsername(String name);

}
