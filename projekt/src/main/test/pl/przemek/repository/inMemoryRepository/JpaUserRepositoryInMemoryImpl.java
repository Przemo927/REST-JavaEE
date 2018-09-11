package pl.przemek.repository.inMemoryRepository;

import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepository;

import javax.jws.soap.SOAPBinding;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class JpaUserRepositoryInMemoryImpl implements JpaUserRepository {

    private List<User> listOfUsers;

    public JpaUserRepositoryInMemoryImpl(){
        listOfUsers=new ArrayList<>();
        populateUserList();
    }
    @Override
    public void add(User user) {
        listOfUsers.add(user);
    }

    @Override
    public void remove(User user) {
        listOfUsers.remove(user);
    }

    @Override
    public User update(User user) {
        for(int i=0;i<listOfUsers.size();i++){
            if(user.getId()==listOfUsers.get(i).getId())
                listOfUsers.set(i,user);
        }
        return user;
    }

    @Override
    public Integer updateWithoutPassword(User user) {
        int updatedCount=0;
        for(int i=0;i<listOfUsers.size();i++){
            if(user.getId()==listOfUsers.get(i).getId()){
                User updatedUser=listOfUsers.get(i);
                updatedUser.setUsername(user.getUsername());
                updatedUser.setEmail(user.getEmail());
                updatedUser.setActive(user.isActive());
                listOfUsers.set(i,updatedUser);
                updatedCount+=1;
            }
        }
        return updatedCount;
    }

    @Override
    public User get(Class<User> clazz, long id) {
        for (User listOfUser : listOfUsers) {
            if (listOfUser.getId() == id) {
                return listOfUser;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll(String nameOfQuery, Class<User> clazz) {
        return listOfUsers;
    }

    @Override
    public List<User> getUserByUsername(String name) {
        List<User> temporaryList=new ArrayList<>();
        for(User user:listOfUsers){
            if(name!=null && name.equals(user.getUsername()))
                temporaryList.add(user);
        }
        return listOfUsers;
    }

    @Override
    public boolean checkPresenceOfUserByUsername(String username) {
        for(User user:listOfUsers){
            if(username!=null && username.equals(user.getUsername()))
                return true;
        }
        return false;
    }

    @Override
    public boolean checkPresenceOfEmail(String email) {
        for(User user:listOfUsers){
            if(email!=null && email.equals(user.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public boolean updateLastLogin(String username) {
        for(User user:listOfUsers){
            if(user.getUsername().equals(username)){
                user.setLastLogin(new Timestamp(new Date().getTime()));
                return true;
            }
        }
        return false;
    }

    private void populateUserList(){
        User user;
        for(int i=0;i<10;i++){
            user=new User();
            user.setEmail("email"+new Random().nextInt(9)+"@gmail.com");
            user.setId(i);
            user.setPassword("password"+i);
            user.setUsername("username"+i);
            user.setActive(true);
            listOfUsers.add(user);

        }
    }
}
