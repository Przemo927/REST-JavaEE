package pl.przemek.rest;

import pl.przemek.model.User;
import pl.przemek.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/user")
public class UserEndPoint {

    @Inject
    private UserService userservice;

    @Path("/{username}")
    @DELETE
    public void removeByUserName(@PathParam("username") String username) {
        userservice.RemoveByUserName(username);
    }

    @PUT
    public void updateUser(User user){
        userservice.updateUser(user);
    }
}
