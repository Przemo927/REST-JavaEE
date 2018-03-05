package pl.przemek.rest;


import pl.przemek.security.ClientResponseFilter;
import pl.przemek.security.Login;
import pl.przemek.security.TokenService;
import pl.przemek.security.TokenStore;
import pl.przemek.service.UserService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.LOCATION;

@Path("/login")
public class LoginEndPoint {
    private final static String HOMEPATH="/projekt/index.html#!/home";
    private final static String LOGINPATH="http://localhost:8080/projekt/j_security_check";

    private Logger logger;

    private UserService userService;

    private TokenService tokenService;

    private TokenStore tokenStore;

    private HttpServletRequest request;

    private final Client CLIENT = ClientBuilder.newClient();

    public LoginEndPoint(){}
    @Inject
    public LoginEndPoint(Logger logger,UserService userService,TokenService tokenService,TokenStore tokenStore,HttpServletRequest request){
        this.logger=logger;
        this.userService=userService;
        this.tokenService=tokenService;
        this.tokenStore=tokenStore;
        this.request=request;
    }

@Login
@POST
public Response login(@FormParam("j_username") String username,@FormParam("j_password") String password) throws IOException, URISyntaxException {

    String encryptedPassword=userService.encryptPassword(password);
    String token=tokenService.generateToken(username,encryptedPassword);

    logger.log(Level.INFO, "Generated token is {0}", token);

    MultivaluedMap<String,String> multivaluedMap=new MultivaluedHashMap<>();
    multivaluedMap.add("j_username",username);
    multivaluedMap.add("j_password",password);
    WebTarget target = CLIENT.target(LOGINPATH);
    Response response1=target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(Entity.form(multivaluedMap));
    if(response1==null){
    	logger.log(Level.INFO,"Response is null");
    }
    tokenStore.setToken("Bearer "+token);
    return Response.fromResponse(response1).header(LOCATION,HOMEPATH).header(AUTHORIZATION,"Bearer "+token).status(302).build();
}
}
