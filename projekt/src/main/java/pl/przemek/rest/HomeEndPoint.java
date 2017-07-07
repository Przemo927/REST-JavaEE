package pl.przemek.rest;

import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.model.Connection;
import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.DiscoveryRepository;
@RequestScoped
@Path("/home")
public class HomeEndPoint {


@GET
@Path("/check")
@Produces(MediaType.APPLICATION_JSON)
public Connection login (@Context HttpServletRequest request){
	Connection con= new Connection();
	con.setName("Zaloguj");
	con.setPath("/projekt/api/login");
	if(request.getUserPrincipal() != null){
		con.setName("Wyloguj");
		con.setPath("/projekt/api/logout");	
	}

	return con;
}

}
