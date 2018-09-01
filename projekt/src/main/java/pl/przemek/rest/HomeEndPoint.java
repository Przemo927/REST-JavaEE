package pl.przemek.rest;

import org.json.simple.JSONObject;
import pl.przemek.mapper.ExceptionMapperAnnotation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/home")
@ExceptionMapperAnnotation
public class HomeEndPoint {
	private final static String LOGIN_PATH = "/projekt";
	private final static String LOGOUT_PATH= "/projekt/api/logout";

	@Inject
	private  HttpServletRequest request;

	@GET
	@Path("/check")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginStatus (){
			JSONObject json = new JSONObject();
		if(request.getUserPrincipal() != null) {
			json.put("name", "Logout");
			json.put("path", LOGOUT_PATH);
			if (request.isUserInRole("admin")) {
				json.put("role", "admin");
			}
			else{
				json.put("role","user");
			}
		}
		else{
			json.put("name","Login");
			json.put("path",LOGIN_PATH);
		}
		return Response.ok(json).build();
	}

}

