package pl.przemek.service;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.JpaRoleRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.security.PasswordSecurity;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    private JpaUserRepository userRepo;
    private JpaRoleRepository rolRepo;
    private Logger logger;
    @Inject
    public UserService(Logger logger,JpaUserRepository userRepo, JpaRoleRepository rolRepo){
        this.logger=logger;
        this.userRepo=userRepo;
        this.rolRepo=rolRepo;
    }
    public UserService(){
        this.userRepo=null;
        this.rolRepo=null;
    }

    public void addUser(User user) {
        if(user!=null) {
            user.setActive(true);
            String password = user.getPassword();
            String hashedPassword = PasswordSecurity.hashPassword(password);
            user.setPassword(hashedPassword);
            userRepo.add(user);
            addRole(user);
        }else {
            logger.log(Level.WARNING,"[UserService] addUser() user is null");
        }
    }

    void addRole(User user) {
        List<Role> listOfRoles=rolRepo.getRoles("user");
        if(!listOfRoles.isEmpty()){
            Role role = listOfRoles.get(0);
            rolRepo.update(role, user);
        }
    }

    public Optional<User> getUserById(long id){
        User user=userRepo.get(User.class,id);
        return Optional.ofNullable(user);
    }

    public void removeByUserId(long id) {
        User user;
        user=userRepo.get(User.class,id);
        if(user!=null)
            userRepo.remove(user);
        else logger.log(Level.WARNING,"[UserService] removeByUserId() user wasn't found id="+id);
    }

    void updateUser(User user){
        if(user==null){
            logger.log(Level.SEVERE,"[UserService] updateUser() user is null");
        }else{
            userRepo.update(user);
        }
    }

    public void updateUserWithoutPassword(User user){
        if(user==null){
            logger.log(Level.SEVERE,"[UserService] updateUserWithoutPassword() user is null");
        }else{
            userRepo.updateWithoutPassword(user);
        }
    }

    public List<User> getAllUsers(){
        return userRepo.getAll("User.findAll", User.class);
    }

}
