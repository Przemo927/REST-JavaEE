package pl.przemek.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.Message.EmailMessageTemplate;
import pl.przemek.Message.MailService;
import pl.przemek.Message.MessageWrapper;
import pl.przemek.model.SecurityKey;
import pl.przemek.model.User;
import pl.przemek.security.AuthenticationDataStore;
import pl.przemek.security.KeyDataStore;
import pl.przemek.security.Utils.KeyUtils;
import pl.przemek.service.SecurityKeyService;
import pl.przemek.service.UserService;

import java.net.URI;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;

@Produces(MediaType.APPLICATION_JSON)
@Path("/register")
public class RegisterEndPoint {


    private UserService userService;
    private MailService mailService;
    private HttpServletRequest request;
    private SecurityKeyService keyService;
    private KeyDataStore keyStore;

    @Inject
    public RegisterEndPoint(UserService userService, MailService mailService, HttpServletRequest request,SecurityKeyService keyService,KeyDataStore keyStore){
        this.userService=userService;
        this.mailService=mailService;
        this.request=request;
        this.keyService=keyService;
        this.keyStore=keyStore;
    }
    public RegisterEndPoint(){}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendMessageToRegistration(@Valid User user) throws NoSuchProviderException, NoSuchAlgorithmException {
        EmailMessageTemplate template=new EmailMessageTemplate();
        String message=template.getPreparedMessage(user.getUsername());
        KeyPair keyPair=KeyUtils.generatePairOfKeys();
        String publicKeyAsString=KeyUtils.convertKeyToString(keyPair.getPublic());
        MessageWrapper msg = wrapMessage(message,user,publicKeyAsString);
        mailService.sendMessage(msg);
        keyStore.setPrivateKey(keyPair.getPrivate());
        setUpSessionForRegistration(user);

    }
    MessageWrapper wrapMessage (String message,User user, String publicKey){
        return new MessageWrapper(message,user,publicKey);
    }
    void setUpSessionForRegistration(User user){
        HttpSession session=request.getSession(true);
        session.setMaxInactiveInterval(300);
        session.setAttribute(user.getUsername(),user);
    }
    @GET
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(@PathParam("username") String username) throws Exception {
        User user=(User)request.getSession(false).getAttribute(username);
        userService.addUser(user);
        addPrivateKeyToDatabase(keyStore.getPrivateKey(),user.getUsername());
        return Response.seeOther(new URI("http://localhost:8080/projekt"))
                .build();
    }
    void addPrivateKeyToDatabase(PrivateKey key, String userame){
        String privateKeyAsString=KeyUtils.convertKeyToString(key);
        SecurityKey securityKey=new SecurityKey();
        securityKey.setUsername(userame);
        securityKey.setPrivateKey(privateKeyAsString);
        keyService.addPrivateKey(securityKey);
    }

}

