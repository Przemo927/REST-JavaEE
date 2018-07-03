package pl.przemek.service;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.JpaRoleRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.security.PasswordSecurity;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserService {

    private JpaUserRepository userRepo;
    private JpaRoleRepository rolRepo;
    @Inject
    public UserService(JpaUserRepository userRepo, JpaRoleRepository rolRepo){
        this.userRepo=userRepo;
        this.rolRepo=rolRepo;
    }
    public UserService(){
        this.userRepo=null;
        this.rolRepo=null;
    }


    public void addUser(User user) throws Exception {
        if(user!=null) {
            user.setActive(true);
            String password = user.getPassword();
            String hashedPassword = hashPassword(password);
            user.setPassword(hashedPassword);
            userRepo.add(user);
            addRole(user);
        }
    }

    void addRole(User user) throws Exception {
        List<Role> listOfRoles=rolRepo.getRoles("user");
        if(!listOfRoles.isEmpty()){
            Role role = listOfRoles.get(0);
            rolRepo.update(role, user);
        }
    }
    public User getUserById(long id){
        return userRepo.get(User.class,id);
    }

    public void removeByUserId(long id) {
        User user=userRepo.get(User.class,id);
        if(user!=null)
            userRepo.remove(user);
    }

    public void updateUser(User user){
        userRepo.update(user);
    }

    public void updateUserWithoutPassword(User user){
        userRepo.updateWithoutPassword(user);
    }

    public List<User> getAllUsers(){
        return userRepo.getAll("User.findAll", User.class);
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        PasswordSecurity security=new PasswordSecurity();
        return security.hashPassword(password);
    }

}
