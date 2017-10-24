package pl.przemek.service;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.JpaRoleRepository;
import pl.przemek.repository.JpaUserRepository;

import javax.inject.Inject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserService {

    private JpaUserRepository userRepo;
    private JpaRoleRepository rolRepo;
    @Inject
    public UserService(JpaUserRepository userRepo,JpaRoleRepository rolRepo){
        this.userRepo=userRepo;
        this.rolRepo=rolRepo;
    }
    public UserService(){
        this.userRepo=null;
        this.rolRepo=null;
    }


    public User addUser(User user) throws Exception {
        user.setActive(true);
        String password=user.getPassword();
        String md5Pass = encryptPassword(password);
        user.setPassword(md5Pass);
        userRepo.add(user);
        addRole(user);
        return user;
    }

    void addRole(User user) throws Exception {
        Role role=rolRepo.get("user");
        rolRepo.update(role, user);
    }
    public User getUserById(long id){
        return userRepo.get(id);
    }

    public void removeByUserId(long id) {
        User user=userRepo.get(id);
        userRepo.remove(user);
    }

    public void updateUser(User user){
        userRepo.update(user);
    }
    public List<User> getAllUsers(){
        return userRepo.getAll();
    }

    String encryptPassword(String password) {
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

}
