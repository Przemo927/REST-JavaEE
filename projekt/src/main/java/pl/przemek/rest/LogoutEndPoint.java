package pl.przemek.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/logout")
public class LogoutEndPoint {

@GET
public void Logout(@Context HttpServletRequest request,@Context HttpServletResponse response) throws IOException{
request.getSession().invalidate();
response.sendRedirect("/projekt/index.html#!/home");
}
}
