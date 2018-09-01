package pl.przemek.rest;

import com.sun.org.apache.regexp.internal.RE;
import pl.przemek.model.User;
import pl.przemek.service.UserService;
import pl.przemek.wrapper.ResponseMessageWrapper;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/user")
public class UserEndPoint {


    private UserService userservice;
    private Logger logger;

    @Inject
    public UserEndPoint(Logger logger,UserService userService){
        this.logger=logger;
        this.userservice=userService;
    }
    public UserEndPoint(){}

    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    public Response removeByUserName(@PathParam("id") long id) {
        userservice.removeByUserId(id);
        return Response.ok(ResponseMessageWrapper.wrappMessage("User was removed")).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id){
        Optional<User> userOptional=userservice.getUserById(id);
        return userOptional.map(user -> {
            return Response.ok(user).build();
        }).orElseGet(()->{
            logger.log(Level.SEVERE,"[UserService] getUserById() user wasn't found");
            return Response.status(Response.Status.NO_CONTENT).build();
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user){
        if(user!=null) {
            userservice.updateUserWithoutPassword(user);
            return Response.ok("User was updated").build();
        }else {
            logger.log(Level.SEVERE,"[UserService] updateUser() user wasn't updated");
            return Response.status(Response.Status.BAD_REQUEST).entity("User wasn't found").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(){
        List<User> listOfUsers=userservice.getAllUsers();
        if(listOfUsers.isEmpty()) {
            logger.log(Level.SEVERE,"[UserService] getAllUsers() users weren't found");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
       return Response.ok(listOfUsers).build();
    }

}
