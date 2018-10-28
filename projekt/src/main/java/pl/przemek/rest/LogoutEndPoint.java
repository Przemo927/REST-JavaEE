package pl.przemek.rest;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/logout")
public class LogoutEndPoint {
    private final static String HOME_PATH="/projekt";

    private Logger logger;

    @Inject
    public LogoutEndPoint(Logger logger){
        this.logger=logger;
    }
    public LogoutEndPoint(){}
    @GET
    public void logout(@Context HttpServletRequest request,@Context HttpServletResponse response){
        try {
            request.getSession().invalidate();
            request.logout();
            response.sendRedirect(HOME_PATH);
        } catch (IOException | ServletException e) {
            logger.log(Level.SEVERE,"[LoginEndPoint] logout()",e);
        }
    }
}
