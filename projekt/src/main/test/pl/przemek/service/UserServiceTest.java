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

import javax.jws.soap.SOAPBinding;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyBoolean;
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
    public void shouldExecuteMethodAddOfUserRepository() throws Exception {
        User user=mock(User.class);
        when(user.getPassword()).thenReturn(anyString());
        userService.addUser(user);
        verify(userRepo).add(user);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenUserIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("User is null");
        userService.addUser(null);

    }

    @Test
    public void ShouldUseTheSameObjectOfUser() throws Exception {
        User user=mock(User.class);

        when(user.getPassword()).thenReturn(anyString());
        userService.addUser(user);

        verify(userRepo).add(user);
        verify(userRepo,never()).add(not(eq(user)));
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

        verify(rolRepo).update(isA(Role.class),isA(User.class));
    }

    @Test
    public void shouldExecuteMethodGetOfUserRepository() throws Exception{
            userService.getUserById(anyLong());
            verify(userRepo).get(anyLong());
    }

    @Test
    public void shoudlUseTheSameValueOfId() throws Exception{
        long id=12345;
        userService.getUserById(id);
        verify(userRepo).get(id);
        verify(userRepo,never()).get(not(eq(id)));
    }
    public Object[] invalidValuesOfId(){
        return $(-1,1,0,1234,12344,12346);
    }

    @Test
    @Parameters(method = "invalidValuesOfId")
    public void shouldNotUseOtherValuesOfId(long value) throws Exception{
       long id=12345;
       userService.getUserById(id);
       verify(userRepo,never()).get(value);
    }

    @Test
    public void shouldExecuteMethodRemoveOfUserRepository() throws Exception{
        User user=mock(User.class);
        when(userRepo.get(anyLong())).thenReturn(user);
        userService.removeByUserId(anyLong());
        verify(userRepo).remove(user);
    }

    @Test
    public void shouldUseTheSameValueOfId() throws Exception{
        long id=1234;
        userService.removeByUserId(id);
        verify(userRepo).get(id);
        verify(userRepo,never()).get(not(eq(id)));
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
        verify(userRepo).getAll();
    }

    public Object[] stringsBeforeAndAfterMd5Hashing(){
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
    }


}