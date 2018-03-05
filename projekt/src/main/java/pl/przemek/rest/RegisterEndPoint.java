package pl.przemek.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.Message.MailService;
import pl.przemek.Message.MessageWrapper;
import pl.przemek.model.User;
import pl.przemek.service.UserService;

import java.net.URI;

@Produces(MediaType.APPLICATION_JSON)
@Path("/register")
public class RegisterEndPoint {


    private UserService userService;
    private MailService mailService;
    private User user;
    private HttpServletRequest request;
    @Inject
    public RegisterEndPoint(UserService userService, MailService mailService, HttpServletRequest request){
        this.userService=userService;
        this.mailService=mailService;
        this.request=request;
    }
    public RegisterEndPoint(){}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendEmailAndSaveUserToRegistration(@Valid User user) {
        String message="Click link below to continue registration"+
                        "<br><br><a href=http://localhost:8080/projekt" +
                        "/api/register/"+user.getUsername()+">Continue</a>";
        MessageWrapper msg = wrapMessage(message,user);
        mailService.sendMessage(msg);
        request.getSession(true).setAttribute(user.getUsername(),user);

    }
    MessageWrapper wrapMessage (String message,User user){
        return new MessageWrapper(message,user);
    }
    @GET
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(@PathParam("username") String username) throws Exception {
        User user=(User)request.getSession(false).getAttribute(username);
        this.user=userService.addUser(user);
        return Response.seeOther(new URI("http://localhost:8080/projekt"))
                .build();
    }

}

