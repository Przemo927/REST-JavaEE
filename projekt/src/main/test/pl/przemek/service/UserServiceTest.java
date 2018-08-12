package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.JpaRoleRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.repository.inMemoryRepository.JpaRoleRepositoryInMemoryImpl;
import pl.przemek.repository.inMemoryRepository.JpaUserRepositoryInMemoryImpl;

import java.util.*;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class UserServiceTest {
    private JpaUserRepository userRepo;
    private JpaRoleRepository rolRepo;
    private UserService userService;

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Before
    public void setUp(){
        userRepo=mock(JpaUserRepository.class);
        rolRepo=mock(JpaRoleRepository.class);
        userService=new UserService(userRepo,rolRepo);

    }
    @Test
    public void shouldAddUser() throws Exception {
        createUserServiceObjectWithInMemoryLayer();
        String password="password";
        long id=1234;
        int amountOfusers=userRepo.getAll("",User.class).size();
        User user=new User();
        user.setPassword(password);
        user.setId(id);

        assertTrue(userRepo.get(User.class,id)==null);

        userService.addUser(user);

        assertEquals(userRepo.getAll("",User.class).size(),amountOfusers+1);
        assertTrue(userRepo.get(User.class,id)!=null);
        assertEquals(userRepo.get(User.class,id).getId(),id);
    }
    @Test
    public void shouldAddUserToRole() throws Exception {
        createUserServiceObjectWithInMemoryLayer();
        User user=new User();
        String name="Name";
        user.setUsername(name);
        assertEquals(0,rolRepo.getRoles("user").get(0).getUsers().size());

        userService.addUser(user);

        Role userRole=rolRepo.getRoles("user").get(0);
        assertEquals(1,userRole.getUsers().size());
        assertTrue(userRole.getUsers().contains(user));

    }
    @Test
    public void shouldDoNotAddUserAndRoleWhenUserIsNull() throws Exception {
        createUserServiceObjectWithInMemoryLayer();
        int amountOfusers=userRepo.getAll("",User.class).size();
        List<User> listOfUsers=userRepo.getAll("",User.class);

        userService.addUser(null);

        assertEquals(userRepo.getAll("",User.class).size(),amountOfusers);
        assertEquals(listOfUsers,userRepo.getAll("",User.class));
        assertEquals(0,rolRepo.getRoles("user").get(0).getUsers().size());
    }
    @Test
    public void shouldNotAddUserToRoleWhenRoleDidNotFind() throws Exception {
        createUserServiceObjectWithInMemoryLayer();
        ((JpaRoleRepositoryInMemoryImpl)rolRepo).deleteRoleByRoleName("user");
        User user=new User();
        user.setId(1234);

        userService.addRole(user);

        assertTrue(rolRepo.getRoles("user").isEmpty());

    }

    @Test
    public void shouldExecuteGetMethodOfUserRepository(){
        createUserServiceObjectWithInMemoryLayer();
        long id=1234;
        User user=new User();
        user.setId(id);

        assertEquals(null,userService.getUserById(id));
        userRepo.add(user);

        assertEquals(user,userService.getUserById(id));
    }
    @Test
    public void shouldNotRemoveUserIfDidNotSave(){
        createUserServiceObjectWithInMemoryLayer();
        User user=new User();
        user.setId(1234);
        int amountOfUsers=userRepo.getAll("",User.class).size();
        assertEquals(null,userService.getUserById(1234));

        userService.removeByUserId(1234);

        assertEquals(amountOfUsers,userRepo.getAll("",User.class).size());
    }

    @Test
    public void shouldRemoveUserIfSaved(){
        createUserServiceObjectWithInMemoryLayer();
        long id=1234;
        User user=new User();
        user.setId(id);
        userRepo.add(user);
        int amountOfUsers=userRepo.getAll("",User.class).size();

        userService.removeByUserId(id);

        assertEquals(amountOfUsers-1,userRepo.getAll("",User.class).size());
        assertEquals(null,userRepo.get(User.class,id));
    }
    private void createUserServiceObjectWithInMemoryLayer(){
        userRepo=new JpaUserRepositoryInMemoryImpl();
        rolRepo=new JpaRoleRepositoryInMemoryImpl();
        userService=new UserService(userRepo,rolRepo);
    }


    @Test
    public void shouldNotExecuteMethodUpdateOfRoleRepositoryWhenListOfRolesIsEmpty() throws Exception {
        User user=mock(User.class);
        Role role=mock(Role.class);
        List<Role> roleList=new ArrayList<>();

        when(rolRepo.getRoles(anyString())).thenReturn(roleList);
        userService.addRole(user);

        verify(rolRepo, never()).update(isA(Role.class),isA(User.class));
    }
    @Test
    public void shouldExecuteMethodUpdateOfRoleRepositoryWhenListOfRolesIsNotEmpty() throws Exception {
        User user=mock(User.class);
        Role role=mock(Role.class);
        List<Role> roleList=new ArrayList<>();
        roleList.add(new Role());

        when(rolRepo.getRoles(anyString())).thenReturn(roleList);
        userService.addRole(user);

        verify(rolRepo,times(1)).update(isA(Role.class),isA(User.class));
    }

    @Test
    public void shouldExecuteMethodGetOfUserRepository() throws Exception{
            userService.getUserById(1);
            verify(userRepo).get(eq(User.class),anyLong());
    }

    public Object[] userId(){
        return $(1,123456,654321,0);
    }
    @Test
    @Parameters(method = "userId")
    public void shoudlUseTheSameValueOfId(long id) throws Exception{
        userService.getUserById(id);
        verify(userRepo,times(1)).get(User.class,id);
        verify(userRepo,never()).get(eq(User.class),not(eq(id)));
    }

    @Test
    public void shouldExecuteMethodRemoveOfUserRepository() throws Exception{
        User user=mock(User.class);
        when(userRepo.get(eq(User.class),anyLong())).thenReturn(user);
        userService.removeByUserId(1);
        verify(userRepo).remove(user);
    }

    @Test
    @Parameters(method = "userId")
    public void shouldUseTheSameValueOfId(long id) throws Exception{
        userService.removeByUserId(id);
        verify(userRepo).get(User.class,id);
        verify(userRepo,never()).get(eq(User.class),not(eq(id)));
    }

    @Test
    public void shouldDoNotExecuteRemoveMethodWhenUserWasNotFound() throws Exception {
        when(userRepo.get(eq(User.class),anyLong())).thenReturn(null);
        userService.removeByUserId(1);
        verify(userRepo,never()).remove(isA(User.class));
    }

    @Test
    public void shouldExecuteMethodUpateOfUserRepository(){
        User user=mock(User.class);
        userService.updateUser(user);
        verify(userRepo).update(user);
    }

    @Test
    public void shouldExecuteMethodGetAllOfUserRepository(){
        userService.getAllUsers();
        verify(userRepo).getAll(anyString(),eq(User.class));
    }

    /*public Object[] stringsBeforeAndAfterMd5Hashing(){
        return $(new String[] {"","d41d8cd98f00b204e9800998ecf8427e"},
                new String[] {"a","cc175b9c0f1b6a831c399e269772661"},
                new String[] {"abc","900150983cd24fb0d6963f7d28e17f72"},
                new String[] {"message digest","f96b697d7cb7938d525a2f31aaf161d0"},
                new String[] {"abcdefghijklmnopqrstuvwxyz","c3fcd3d76192e4007dfb496cca67e13b"},
                new String[] {"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789","d174ab98d277d9f5a5611c2c9f419d9f"},
                new String[] {"12345678901234567890123456789012345678901234567890123456789012345678901234567890","57edf4a22be3c955ac49da2e2107b67a"});
    }
    @Test
    @Parameters(method = "stringsBeforeAndAfterMd5Hashing")
    public void shouldEqualsStringAfterHashingAndResultOfEncryptPasswordMethodWithStringBeforeHashing(String beforeHashing,String afterHashing){
        assertEquals(afterHashing,userService.encryptPassword(beforeHashing));
    }*/


}