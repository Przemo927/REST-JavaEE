package pl.przemek.rest;

import com.sun.org.apache.regexp.internal.RE;
import pl.przemek.Message.MailService;
import pl.przemek.Message.MessageWrapper;
import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.service.UserService;
import pl.przemek.wrapper.ResponseMessageWrapper;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;

@Path("/user")
public class UserEndPoint {


    private UserService userservice;
    private JpaUserRepository userrepo;
    private MailService mailService;
    private final static ResponseMessageWrapper mw=new ResponseMessageWrapper();

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
    public Response removeByUserName(@PathParam("id") long id) {
        userservice.removeByUserId(id);
        return Response.ok(mw.wrappMessage("User was removed")).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id){
        User user=userservice.getUserById(id);
        if(user==null){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(user).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user){
        userservice.updateUserWithoutPassword(user);
        return Response.ok("User was updated").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(){
        List<User> listOfUsers=userservice.getAllUsers();
        if(listOfUsers.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
       return Response.ok(listOfUsers).build();
    }

}
