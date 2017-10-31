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

    private CommentService commentservice;
    @Inject
    HttpServletRequest request;

    @Inject
    public CommentEndPoint(CommentService commentservice){
        this.commentservice=commentservice;
    }
    public CommentEndPoint(){}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getAllComment(){
        commentservice.getAllComment();
        return commentservice.getAllComment();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public void addComment(Comment comment,@PathParam("id") long id){
        User user=(User)request.getSession(false).getAttribute("user");
        comment.setUser(user);
        commentservice.addComment(comment,id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public List<Comment> getByDiscoveryId(@PathParam("id") long id){
        return commentservice.getByDiscoveryId(id);
    }
}
