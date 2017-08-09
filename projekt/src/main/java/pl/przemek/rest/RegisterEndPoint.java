package pl.przemek.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.Message.MailService;
import pl.przemek.Message.MessageWrapper;
import pl.przemek.model.User;
import pl.przemek.service.UserService;

@Produces(MediaType.APPLICATION_JSON)
@Path("/register")
public class RegisterEndPoint {

    @Inject
    UserService userService;
    @Inject
    private MailService mailService;

    private User user;
    @Context
    HttpServletRequest request;
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response sendEmailAndSaveUserToRegistration(@Valid User user) {
	//this.user=userService.addUser(user);
    String message="Click link below to continue registration"+"</br>"+"<a href=http://localhost:8080/projekt/api/register/"+user.getUsername()+">Klik</a>";
    MessageWrapper msg = new MessageWrapper(message,user);
    mailService.sendMessage(msg);
    request.getSession(true).setAttribute(user.getUsername(),user);
    //User user1=(User)request.getSession(false).getAttribute("userToRegistration");
    return Response
          .accepted(this.user)
        .build();
}
    @GET
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(@PathParam("username") String username) {
        User user=(User)request.getSession(false).getAttribute(username);
        this.user=userService.addUser(user);
        return Response
                .accepted(this.user)
                .build();
    }

}

