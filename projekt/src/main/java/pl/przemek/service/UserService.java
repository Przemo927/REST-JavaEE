package pl.przemek.service;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.JpaRoleRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.security.PasswordSecurity;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
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

    public void addUser(User user) throws Exception {
        try {
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
        }catch (Exception e){
            logger.log(Level.SEVERE,"[UserService] addUser()",e);
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
        User user=null;
        try {
            user=userRepo.get(User.class,id);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[UserService] getUserById() id="+id,e);
            return null;
        }
        return user;
    }

    public void removeByUserId(long id) {
        User user=null;
        try {
            user=userRepo.get(User.class,id);
            if(user!=null)
                userRepo.remove(user);
            else logger.log(Level.WARNING,"[UserService] removeByUserId() user wasn't found id="+id);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[UserService] removeByUserId()",e);
        }
    }

    public void updateUser(User user){
        try {
            userRepo.update(user);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[UserService] updateUser()",e);
        }
    }

    public void updateUserWithoutPassword(User user){
        try {
            userRepo.updateWithoutPassword(user);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[UserService] updateUserWithoutPassword()",e);
        }
    }

    public List<User> getAllUsers(){
        List<User> listOfUsers=null;
        try {
            listOfUsers=userRepo.getAll("User.findAll", User.class);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[UserService] addUser()",e);
            return Collections.emptyList();
        }
        return listOfUsers;
    }

}
