package pl.przemek.repository;

import pl.przemek.model.User;

import java.util.List;

public interface UserRepository {
    public void add(User user);
    public void remove(User user);
    public User update(User user);
    public User get(Long id);
    public List<User> getAll();
    public User getUserByUsername(String name);

}
