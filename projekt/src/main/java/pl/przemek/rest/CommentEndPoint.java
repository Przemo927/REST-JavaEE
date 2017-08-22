package pl.przemek.rest;

import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.service.CommentService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/comment")
public class CommentEndPoint {

    @Inject
    private CommentService commentservice;
    @Inject
    HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getAllComment(){
        return commentservice.getAllComment();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public void addComment(Comment comment,@PathParam("name") String name){
        User user=(User)request.getSession(false).getAttribute("user");
        comment.setUser(user);
        commentservice.addComment(comment,name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public List<Comment> getByDiscoveryId(@PathParam("name") String name){
        return commentservice.getByDiscoveryName(name);
    }
}
