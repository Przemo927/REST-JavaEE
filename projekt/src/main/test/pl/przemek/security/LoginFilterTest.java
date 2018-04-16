package pl.przemek.security;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
@PrepareForTest({Response.class})
public class LoginFilterTest {

    @Rule
    //Zeby dzialaly Parametry wraz z MockitoJUnitRunner
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    /*@Rule
    public PowerMockRule rule1 = new PowerMockRule();*/

    @Spy
    @InjectMocks
    private LoginFilter loginFilter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private JpaUserRepositoryImpl userrep;
    @Mock
    private TokenService tokenService;
    @Mock
    private TokenStore tokenStore;
    @Spy
    private AuthenticationDataStore userDataStore;
    @Mock
    private JwtsRepository jwtsRepository;

    private ContainerRequestContext containerRequestContext;

    @Before
    public void signUp(){
        containerRequestContext=mock(ContainerRequestContext.class);
    }

    @Test
    public void doNothingIfRequestGetSessionIsNull() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        loginFilter.filter(containerRequestContext);
        verify(loginFilter,never()).checkToken(anyString(),anyString());
        verify(loginFilter,never()).saveToken(anyString(),anyString());
        verify(containerRequestContext,never()).getHeaderString(anyString());
    }

    @Test
    public void checkTokenAndSaveTokenIfLoggedUserIsSavedInSession() throws Exception {
        HttpSession session=mock(HttpSession.class);
        String encryptedPassword="encrypted";
        String userName="username";
        String header="header";

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn("");
        when(containerRequestContext.getHeaderString(anyString())).thenReturn(header);
        when(userDataStore.getEncryptedPassword()).thenReturn(encryptedPassword);
        when(userDataStore.getUsername()).thenReturn(userName);
        doNothing().when(loginFilter).checkToken(anyString(),anyString());
        doNothing().when(loginFilter).saveToken(anyString(),anyString());

        loginFilter.filter(containerRequestContext);

        verify(containerRequestContext,times(1)).getHeaderString(anyString());
        verify(loginFilter,times(1)).checkToken(anyString(),anyString());
        verify(loginFilter,times(1)).saveToken(anyString(),anyString());
    }

    public Object[] arguments(){
        return $(new String[]{"0000","11111","22222"},new String[]{"PASSWORD","USERNAME","TOKEN"},new String[]{"password","username","12345e12s45rr123t45f12c1"});
    }
    @Test
    @Parameters(method = "arguments")
    public void methodsShouldUseTheSameValueOfTokenEncryptedPasswordAndUserName(String encryptedPassword,String userName,String token) throws IOException {
        HttpSession session=mock(HttpSession.class);
        userDataStore.setEncryptedPassword(encryptedPassword);
        userDataStore.setUsername(userName);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn("");
        when(containerRequestContext.getHeaderString(anyString())).thenReturn(token);

        doNothing().when(loginFilter).checkToken(token,encryptedPassword);
        doNothing().when(loginFilter).saveToken(anyString(),anyString());

        loginFilter.filter(containerRequestContext);

        verify(loginFilter,times(1)).checkToken(token,encryptedPassword);
        verify(loginFilter,times(1)).saveToken(userName,encryptedPassword);
    }

    @Test
    public void shouldGetUserFromDataBaseSaveTokenAndSaveUserInSessionIfUserIsLoggedButNotSavedInSession() throws IOException, URISyntaxException {
        String username="username";
        List listWithUser=mock(List.class);
        SecurityContext securityContext=mock(SecurityContext.class);
        Principal p=mock(Principal.class);
        User user=spy(new User());
        user.setUsername("userName");
        user.setPassword("password");
        HttpSession session=mock(HttpSession.class);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);
        when(containerRequestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.getUserPrincipal()).thenReturn(p);
        when(p.getName()).thenReturn(username);
        when(userrep.getUserByUsername(anyString())).thenReturn(listWithUser);
        when(listWithUser.get(0)).thenReturn(user);
        doNothing().when(loginFilter).LogoutIfInActiveStatus(isA(User.class),eq(request));
        doNothing().when(loginFilter).saveToken(anyString(),anyString());

        loginFilter.filter(containerRequestContext);

        verify(loginFilter,times(1)).saveToken(anyString(),anyString());
        verify(loginFilter,times(1)).saveUserData(isA(User.class));
        verify(loginFilter,times(1)).LogoutIfInActiveStatus(isA(User.class),eq(request));
        verify(loginFilter,times(1)).saveUserInSession(eq(request),isA(User.class));
    }

    @Test
    public void shouldDoNotSaveTokenSaveUserDataAndSaveUserInSessionIfUserWasNotFound() throws IOException, URISyntaxException {
        SecurityContext securityContext=mock(SecurityContext.class);
        HttpSession session=mock(HttpSession.class);
        String userName="username";
        Principal p=mock(Principal.class);
        List<User> foundUser=new ArrayList<>();

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(containerRequestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.getUserPrincipal()).thenReturn(p);
        when(p.getName()).thenReturn(userName);
        when(userrep.getUserByUsername(userName)).thenReturn(foundUser);
        loginFilter.filter(containerRequestContext);

        verify(loginFilter,never()).saveToken(anyString(),anyString());
        verify(loginFilter,never()).saveUserData(isA(User.class));
        verify(loginFilter,never()).LogoutIfInActiveStatus(isA(User.class),eq(request));
        verify(loginFilter,never()).saveUserInSession(eq(request),isA(User.class));
    }

    @Test
    public void shouldDoAllMethodWhenUserWasFound() throws IOException, URISyntaxException {
        SecurityContext securityContext=mock(SecurityContext.class);
        HttpSession session=mock(HttpSession.class);
        User user=spy(new User());
        String encryptedPassword="password";
        String userName="userName";
        user.setUsername(userName);
        user.setPassword(encryptedPassword);
        Principal p=mock(Principal.class);
        List<User> foundUser=new ArrayList<>();
        foundUser.add(user);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(containerRequestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.getUserPrincipal()).thenReturn(p);
        when(p.getName()).thenReturn(userName);
        when(userrep.getUserByUsername(userName)).thenReturn(foundUser);
        loginFilter.filter(containerRequestContext);

        verify(loginFilter,times(1)).saveToken(userName,encryptedPassword);
        verify(loginFilter,times(1)).saveUserData(user);
        verify(loginFilter,times(1)).LogoutIfInActiveStatus(user,request);
        verify(loginFilter,times(1)).saveUserInSession(request,user);
    }

    /*@Test
    public void shouldInvalidateSessionAnDoRedirectIfUserIsInActive() throws IOException, URISyntaxException {
        User user=new User();
        PowerMockito.mockStatic(Response.class);
        user.setActive(false);
        HttpSession session=mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        loginFilter.LogoutIfInActiveStatus(user,request);
    }*/
    @Test
    public void shouldSaveUserAsAttributeInSession(){
        User user=mock(User.class);
        HttpSession session=mock(HttpSession.class);

        when(request.getSession(anyBoolean())).thenReturn(session);
        loginFilter.saveUserInSession(request,user);

        verify(session,times(1)).setAttribute("user",user);
    }

    @Test
    public void shouldGenerateTokenAndSetToken(){
        String token="token";
        String userName="userName";
        String password="password";

        when(tokenService.generateToken(anyString(),anyString())).thenReturn(token);
        loginFilter.saveToken(userName,password);

        verify(tokenService,times(1)).generateToken(userName,password);
        verify(tokenStore,times(1)).setToken(token);

    }

    @Test
    public void shouldSavePasswordAndUserNameToUserDataStore(){
        User user=new User();
        String password="password";
        String userName="userName";
        user.setPassword(password);
        user.setUsername(userName);

        loginFilter.saveUserData(user);

        verify(userDataStore,times(1)).setUsername(userName);
        verify(userDataStore,times(1)).setEncryptedPassword(password);
    }

    @Test
    public void shouldDoNotLogOutIfCheckingTokenIsPositive() throws Exception {
        String encryptedPassword="password";
        String token="token";
        HttpSession session=mock(HttpSession.class);
        Key key=mock(Key.class);

        when(tokenService.generateKey(anyString())).thenReturn(key);

        loginFilter.checkToken(anyString(),anyString());

        verify(jwtsRepository,times(1)).checkToken(isA(Key.class),anyString());
        verify(session,never()).invalidate();
        verify(request,never()).logout();
    }

    public Object[] tokenPassword(){
        return $(new String[]{"token","password"},new String[]{"11123","222245"},new String[]{"TOKEN","PASSWORD"});
    }

    @Test
    @Parameters(method = "tokenPassword")
    public void shouldUseTheSameValueOfTokenAndEncryptedPassword(String token,String password) throws Exception {
        HttpSession session=mock(HttpSession.class);
        Key key=mock(Key.class);

        when(tokenService.generateKey(password)).thenReturn(key);

        loginFilter.checkToken(token,password);

        verify(tokenService,times(1)).generateKey(password);
        verify(tokenService,never()).generateKey(token);
        verify(jwtsRepository,times(1)).checkToken(key,token);
        verify(jwtsRepository,never()).checkToken(key,password);
        verify(session,never()).invalidate();
        verify(request,never()).logout();
    }

    @Test
    public void shouldFinishSessionWhenExceptionIsThrown() throws Exception {
        HttpSession session=mock(HttpSession.class);
        doThrow(Exception.class).when(jwtsRepository.checkToken(isA(Key.class),anyString()));
        when(request.getSession()).thenReturn(session);

        verify(request,times(1)).logout();
        verify(session,times(1)).invalidate();
    }

}

