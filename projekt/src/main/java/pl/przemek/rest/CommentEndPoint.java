package pl.przemek.rest;

import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.service.CommentService;
import pl.przemek.wrapper.ResponseMessageWrapper;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@Path("/comment")
public class CommentEndPoint {

    private CommentService commentservice;
    private HttpServletRequest request;
    private final static ResponseMessageWrapper mw=new ResponseMessageWrapper();
    @Context
    UriInfo uriInfo;

    @Inject
    public CommentEndPoint(CommentService commentservice, HttpServletRequest request) {
        this.commentservice = commentservice;
        this.request = request;
    }

    public CommentEndPoint() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllComment() {
        List<Comment> listOfComments=commentservice.getAllComment();
        if(listOfComments.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok(listOfComments).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response addComment(Comment comment, @PathParam("id") long discoveryId) throws IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            comment.setUser(user);
            commentservice.addComment(comment, discoveryId);
            return Response.created(URI.create(uriInfo.getAbsolutePath()+"/"+comment.getId())).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getByDiscoveryId(@PathParam("id") long id) {
        List<Comment> listWithComment=commentservice.getByDiscoveryId(id);
        if(listWithComment.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok(listWithComment).build();
    }

}
