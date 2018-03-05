package pl.przemek.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import pl.przemek.model.LoginStatus;
import pl.przemek.model.Role;
import pl.przemek.model.User;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RequestScoped
@Path("/home")
public class HomeEndPoint {
private final static String loginPath= "/projekt";
private final static String logoutPath= "/projekt/api/logout";

@Inject
private  HttpServletRequest request;

	@GET
@Path("/check")
@Produces(MediaType.APPLICATION_JSON)
public Response loginStatus (){
		JSONObject json = new JSONObject();
	if(request.getUserPrincipal() != null) {
		User user = (User) request.getSession(false).getAttribute("user");
		json.put("name", "Logout");
		json.put("path", logoutPath);
		if (request.isUserInRole("admin")) {
			json.put("role", "admin");
		}
		else{
			json.put("role","user");
		}
	}
	else{
		json.put("name","Login");
		json.put("path",loginPath);
	}
	return Response.ok(json).build();
}

}

