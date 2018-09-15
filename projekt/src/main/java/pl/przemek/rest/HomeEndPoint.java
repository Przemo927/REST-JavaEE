package pl.przemek.rest;

import org.json.simple.JSONObject;
import pl.przemek.mapper.ExceptionMapperAnnotation;
import pl.przemek.wrapper.ResponseMessageWrapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

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
			HashMap<String,Object> map=new HashMap<>();
		if(request.getUserPrincipal() != null) {
			map.put("name", "Logout");
			map.put("path", LOGOUT_PATH);
			if (request.isUserInRole("admin")) {
				map.put("role", "admin");
			}
			else{
				map.put("role","user");
			}
		}
		else{
			map.put("name","Login");
			map.put("path",LOGIN_PATH);
		}
		return Response.ok(ResponseMessageWrapper.wrapMessage(map)).build();
	}

}

