package pl.przemek.security;

import io.jsonwebtoken.Jwts;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class LoginFilterTest {

    @Rule
    //Zeby dzialaly Parametry wraz z MockitoJUnitRunner
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown=ExpectedException.none();

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

    private ContainerRequestContext containerRequestContext;

    @Before
    public void signUp(){
        containerRequestContext=mock(ContainerRequestContext.class);
    }

    @Test
    public void doNothingIfRequestGetSessionIsNull() throws Exception {
        SecurityContext securityContext=getSecurityContext(null);
        when(request.getSession(false)).thenReturn(null);
        when(containerRequestContext.getSecurityContext()).thenReturn(securityContext);

        loginFilter.filter(containerRequestContext);
        verify(loginFilter,never()).ifTokenIsValid(anyString(),anyString());
        verify(loginFilter,never()).saveToken(anyString(),anyString());
        verify(containerRequestContext,never()).getHeaderString(anyString());
    }

    @Test
    public void checkTokenAndSaveTokenIfLoggedUserIsSavedInSession() throws Exception {
        SecurityContext securityContext=getSecurityContext(null);
        HttpSession session=mock(HttpSession.class);
        String encryptedPassword="encrypted";
        String userName="username";
        String header="header";

        when(containerRequestContext.getSecurityContext()).thenReturn(securityContext);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(userName);

        when(containerRequestContext.getHeaderString(anyString())).thenReturn(header);
        when(userDataStore.getEncryptedPassword()).thenReturn(encryptedPassword);
        when(userDataStore.getUsername()).thenReturn(userName);
        doReturn(true).when(loginFilter).ifTokenIsValid(anyString(),anyString());

        loginFilter.filter(containerRequestContext);

        verify(loginFilter,times(1)).ifTokenIsValid(anyString(),anyString());
        verify(loginFilter,times(1)).saveToken(anyString(),anyString());
    }

    @Test
    public void shouldGetUserFromDataBaseSaveTokenAndSaveUserInSessionIfUserIsLoggedButNotSavedInSession() throws IOException, URISyntaxException {
        String username="username";
        List<User> listWithUser=new ArrayList<>();
        SecurityContext securityContext=getSecurityContext(getUserPrincipal(username));
        User user=new User();
        user.setUsername("userName");
        user.setPassword("password");
        user.setActive(true);
        listWithUser.add(user);
        HttpSession session=mock(HttpSession.class);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);
        when(containerRequestContext.getSecurityContext()).thenReturn(securityContext);
        when(userrep.getUserByUsername(anyString())).thenReturn(listWithUser);
        when(userrep.updateLastLogin(anyString())).thenReturn(true);

        loginFilter.filter(containerRequestContext);

        verify(loginFilter,times(1)).saveToken(anyString(),anyString());
        verify(loginFilter,times(1)).saveUserData(isA(User.class));
        verify(loginFilter,times(1)).LogoutIfInActiveStatus(isA(User.class),eq(request));
        verify(loginFilter,times(1)).saveUserInSession(eq(request),isA(User.class));
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
        when(userrep.updateLastLogin(anyString())).thenReturn(true);
        doNothing().when(loginFilter).logout();

        loginFilter.filter(containerRequestContext);

        verify(loginFilter,times(1)).saveToken(userName,encryptedPassword);
        verify(loginFilter,times(1)).saveUserData(user);
        verify(loginFilter,times(1)).LogoutIfInActiveStatus(user,request);
        verify(loginFilter,times(1)).saveUserInSession(request,user);
    }

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

    private SecurityContext getSecurityContext(Principal principal){
        return new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return principal;
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };
    }

    private Principal getUserPrincipal(String user){
        return new Principal() {
            @Override
            public String getName() {
                return user;
            }
        };
    }
}

