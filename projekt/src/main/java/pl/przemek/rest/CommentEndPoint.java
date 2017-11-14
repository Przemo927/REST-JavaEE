package pl.przemek.rest;

import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.service.CommentService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

@Path("/comment")
public class CommentEndPoint {

    private CommentService commentservice;
    private HttpServletRequest request;

    @Inject
    public CommentEndPoint(CommentService commentservice, HttpServletRequest request){
        this.commentservice=commentservice;
        this.request=request;
    }
    public CommentEndPoint(){}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getAllComment(){
        return commentservice.getAllComment();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public void addComment(Comment comment,@PathParam("id") long discoveryId) throws IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session=request.getSession(false);
        User user=(User)session.getAttribute("user");
        comment.setUser(user);
        commentservice.addComment(comment,discoveryId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public List<Comment> getByDiscoveryId(@PathParam("id") long id){
        return commentservice.getByDiscoveryId(id);
    }
}
