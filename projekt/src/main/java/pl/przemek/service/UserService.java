package pl.przemek.service;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.RoleRepository;
import pl.przemek.repository.UserRepository;

import javax.inject.Inject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService {
    @Inject
    private UserRepository userrepo;
    @Inject
    private RoleRepository rolrepo;

    Set<Role> setOfRoles;

    public User addUser(User user){
        user.setActive(true);
        String password=user.getPassword();
        String md5Pass = encryptPassword(password);
        user.setPassword(md5Pass);
        userrepo.add(user);
        Addrole(user);
        return user;
    }

    private void Addrole(User user){
        Role role=rolrepo.get("user");
        rolrepo.update(role, user);
    }
    public void RemoveByUserName(String username) {
        User user=userrepo.getUserByUsername(username);
        userrepo.remove(user);
    }

    public void updateUser(User user){
        String password=user.getPassword();
        String md5Pass = encryptPassword(password);
        user.setPassword(md5Pass);
        userrepo.update(user);
    }

    private String encryptPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.update(password.getBytes());
        String md5Password = new BigInteger(1, digest.digest()).toString(16);
        return md5Password;
    }
    public List<User> getAllUsers(){
        return userrepo.getAll();
    }
}
