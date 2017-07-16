package pl.przemek.rest;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import pl.przemek.model.LoginStatus;
@RequestScoped
@Path("/home")
public class HomeEndPoint {


@GET
@Path("/check")
@Produces(MediaType.APPLICATION_JSON)
public LoginStatus loginStatus (@Context HttpServletRequest request){
	LoginStatus loginsStatus= new LoginStatus();
	loginsStatus.setName("Zaloguj");
	loginsStatus.setPath("/projekt/api/login");
	if(request.getUserPrincipal() != null){
		loginsStatus.setName("Wyloguj");
		loginsStatus.setPath("/projekt/api/logout");	
	}

	return loginsStatus;
}

}
