package pl.przemek.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.RoleRepository;
import pl.przemek.repository.UserRepository;

@Path("/registry")
public class RegistryEndPoint {
	
@Inject
private UserRepository userrepo;
@Inject
private RoleRepository rolrepo;

@POST
@Consumes(MediaType.APPLICATION_JSON)
public void Adduser(User user) throws IOException {
	user.setActive(true);
    userrepo.add(user);   
    Role role=rolrepo.get("user");
    rolrepo.update(role, user);
}
}
