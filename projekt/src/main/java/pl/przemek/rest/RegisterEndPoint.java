package pl.przemek.rest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.RoleRepository;
import pl.przemek.repository.UserRepository;
import pl.przemek.service.UserService;

@Produces(MediaType.APPLICATION_JSON)
@Path("/register")
public class RegisterEndPoint {

    @Inject
    UserService userService;

    User user;
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response addUser(@Valid User user) {
	this.user=userService.addUser(user);

    return Response
          .accepted(this.user)
        .build();
}


}

