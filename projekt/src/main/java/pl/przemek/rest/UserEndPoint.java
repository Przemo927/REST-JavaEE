package pl.przemek.rest;

import pl.przemek.Message.MailService;
import pl.przemek.Message.MessageWrapper;
import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.service.UserService;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Properties;

@Path("/user")
public class UserEndPoint {


    private UserService userservice;
    private JpaUserRepository userrepo;
    private MailService mailService;
    @Inject
    public UserEndPoint(UserService userService,JpaUserRepository userRepository,MailService mailService){
        this.userservice=userService;
        this.userrepo=userRepository;
        this.mailService=mailService;
    }
    public UserEndPoint(){}
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    public void removeByUserName(@PathParam("id") long id) {
        userservice.RemoveByUserId(id);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") Long id){
        return userservice.getUserById(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUser(User user){
        userservice.updateUser(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(){
       return userservice.getAllUsers();
    }

}
