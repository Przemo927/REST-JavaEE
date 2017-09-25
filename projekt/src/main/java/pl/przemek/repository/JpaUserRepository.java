package pl.przemek.repository;

import pl.przemek.model.User;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public interface JpaUserRepository {
    public void add(User user);
    public void remove(User user);
    public User update(User user);
    public User get(Long id);
    public List<User> getAll();
    public User getUserByUsername(String name);
    public boolean checkPresenceOfUserByUsername(String username);
    public boolean checkPresenceOfEmail(String email);
}
