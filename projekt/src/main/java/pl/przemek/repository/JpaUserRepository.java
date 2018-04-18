package pl.przemek.repository;

import pl.przemek.model.User;

import java.util.List;

public interface JpaUserRepository {
    public void add(User user);
    public void remove(User user);
    public User update(User user);
    public Integer updateWithoutPassword(User user);
    public User get(Class<User> clazz,Long id);
    public List<User> getAll(String nameOfQuery,Class<User> clazz);
    public List<User> getUserByUsername(String name);
    public boolean checkPresenceOfUserByUsername(String username);
    public boolean checkPresenceOfEmail(String email);
}
