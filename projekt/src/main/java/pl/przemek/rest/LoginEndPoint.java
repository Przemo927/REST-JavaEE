package pl.przemek.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/login")
public class LoginEndPoint {

@GET
public void Login(@Context HttpServletRequest request,@Context HttpServletResponse response) throws IOException{
	if(request.getUserPrincipal() != null) {
        response.sendRedirect("#home");
    } else {
        response.sendError(403);
    }
}
}
