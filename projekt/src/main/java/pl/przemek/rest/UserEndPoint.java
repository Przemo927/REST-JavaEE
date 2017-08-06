package pl.przemek.rest;

import pl.przemek.model.User;
import pl.przemek.repository.UserRepository;
import pl.przemek.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
public class UserEndPoint {

    @Inject
    private UserService userservice;
    @Inject
    private UserRepository userrepo;

    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    public void removeByUserName(@PathParam("username") String username) {
        userservice.RemoveByUserName(username);
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
