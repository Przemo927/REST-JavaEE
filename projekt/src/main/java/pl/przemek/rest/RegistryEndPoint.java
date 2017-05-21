package pl.przemek.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.RoleRepository;
import pl.przemek.repository.UserRepository;
@Produces(MediaType.APPLICATION_JSON)
@Path("/register")
public class RegistryEndPoint {
	
@Inject
private UserRepository userrepo;
@Inject
private RoleRepository rolrepo;

@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response Adduser(User user,@Context UriInfo uriInfo) {
	user.setActive(true);
	String password=user.getPassword();
	String md5Pass = encryptPassword(password);
	user.setPassword(md5Pass);
	userrepo.add(user);
   Addrole(user);
   
    return Response
            .accepted(user)
            .build();
}

private String encryptPassword(String password) {
    MessageDigest digest = null;
    try {
        digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    digest.update(password.getBytes());
    String md5Password = new BigInteger(1, digest.digest()).toString(16);
    return md5Password;
}

public void Addrole(User user){
	   Role role=rolrepo.get("user");
	   	   rolrepo.update(role, user);
}
}

