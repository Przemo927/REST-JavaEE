package pl.przemek.rest;

import pl.przemek.mapper.ExceptionMapperAnnotation;
import pl.przemek.model.RoleType;
import pl.przemek.rest.utils.ResponseUtils;
import pl.przemek.wrapper.ResponseMessageWrapper;

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

	@Inject
	private  HttpServletRequest request;

	@GET
	@Path("/check")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginStatus (){
			HashMap<String,Object> map=new HashMap<>();
		if(request.getUserPrincipal() != null) {
			map.put("name", "Logout");
			map.put("path", ResponseUtils.getLogoutPath(request));
			if (request.isUserInRole("admin")) {
				map.put("role", RoleType.ADMIN.getRoleType());
			}
			else{
				map.put("role",RoleType.USER.getRoleType());
			}
		}
		else{
			map.put("name","Login");
			map.put("path", ResponseUtils.getHomePath(request));
		}
		return Response.ok(ResponseMessageWrapper.wrapMessage(map)).build();
	}

}

