package pl.przemek.rest;

import pl.przemek.rest.utils.ResponseUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/logout")
public class LogoutEndPoint {

    private Logger logger;

    @Inject
    public LogoutEndPoint(Logger logger){
        this.logger=logger;
    }
    public LogoutEndPoint(){}

    @GET
    public Response logout(@Context HttpServletRequest request){
        try {
            request.getSession().invalidate();
            request.logout();
            return Response.seeOther(new URI(ResponseUtils.getHomePath(request))).build();
        } catch (ServletException | URISyntaxException e) {
            logger.log(Level.SEVERE,"[LoginEndPoint] logout()",e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Something gone wrong").build();
        }
    }
}
