package pl.przemek.security;


import io.jsonwebtoken.ExpiredJwtException;
import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.LOCATION;

@Provider
public class LoginFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    @Inject
    private JpaUserRepository userrep;
    @Inject
    private TokenService tokenService;
    @Inject
    private TokenStore tokenStore;
    @Inject
    private AuthenticationDataStore userDataStore;
    @Inject
    private JwtsRepository jwtsRepository;
    @Inject
    private Logger logger;

    private final static String LOGUT_PATH="/projekt/api/logout";

    @Override
    public void filter(ContainerRequestContext requestContext){
        String username;
    	if(request.getSession(false)!=null && request.getSession(false).getAttribute("user")!=null){
    	    String encryptedPassword=userDataStore.getEncryptedPassword();
    	    username=userDataStore.getUsername();
        	String token=requestContext.getHeaderString(AUTHORIZATION);
            checkToken(token,encryptedPassword);
			saveToken(username,encryptedPassword);
        }

        else if(requestContext.getSecurityContext().getUserPrincipal() != null && request.getSession(false).getAttribute("user") == null) {
            username = requestContext.getSecurityContext().getUserPrincipal().getName();
            List<User> listUserByUsername = userrep.getUserByUsername(username);
            if(!listUserByUsername.isEmpty()) {
                User userByUsername=listUserByUsername.get(0);
                try {
                    LogoutIfInActiveStatus(userByUsername, request);
                } catch (URISyntaxException | IOException e) {
                    logger.log(Level.SEVERE,"[LoginFilter] filter()",e);
                }
                saveToken(userByUsername.getUsername(), userByUsername.getPassword());
                saveUserData(userByUsername);
                saveUserInSession(request, userByUsername);
            } else {
               logout();
            }

        }
    }
    void LogoutIfInActiveStatus(User user, HttpServletRequest request) throws IOException, URISyntaxException {
        if(!user.isActive()){
            logout();
            response.sendRedirect(LOGUT_PATH);
        }
    }
    void logout(){
        request.getSession().invalidate();
        try {
            request.logout();
        } catch (ServletException e) {
            logger.log(Level.SEVERE,"[LoginFilter] logout()",e);
        }
    }
    void saveUserInSession(HttpServletRequest request,User user) {

        request.getSession(false).setAttribute("user", user);
    }
    void saveToken(String username,String password){
    	String token=tokenService.generateToken(username, password);
        tokenStore.setToken(token);
    }
    void saveUserData(User user){
    	userDataStore.setUsername(user.getUsername());
        userDataStore.setEncryptedPassword(user.getPassword());
    }
    void checkToken(String token,String encryptedPassword) {

       try{
            Key key=tokenService.generateKey(encryptedPassword);
            jwtsRepository.checkToken(key,token);
        } catch (ExpiredJwtException e) {
           logger.log(Level.SEVERE,"[LoginFilter] filter() logout");
           logout();
        }

    }
}
