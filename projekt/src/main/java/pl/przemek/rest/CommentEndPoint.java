package pl.przemek.rest;

import pl.przemek.mapper.ExceptionMapperAnnotation;
import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.rest.utils.ResponseUtils;
import pl.przemek.service.CommentService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/comment")
@ExceptionMapperAnnotation
public class CommentEndPoint {

    private CommentService commentservice;
    private HttpServletRequest request;
    private Logger logger;
    @Context
    UriInfo uriInfo;
    @Context
    Request requestRest;

    @Inject
    public CommentEndPoint(Logger logger,CommentService commentservice, HttpServletRequest request) {
        this.logger=logger;
        this.commentservice = commentservice;
        this.request = request;
    }

    public CommentEndPoint() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllComment() {
        List<Comment> listOfComments=commentservice.getAllComment();
        if(listOfComments.isEmpty()) {
            logger.log(Level.SEVERE,"[CommentEndPoint] getAllComment() comments weren't found");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return ResponseUtils.checkIfModifiedAndReturnResponse(listOfComments,requestRest).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@Valid Comment comment) {
        try {
            request.setCharacterEncoding("UTF-8");
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");
            if(user!=null) {
                comment.setUser(user);
                commentservice.addComment(comment);
                return Response.created(URI.create(uriInfo.getAbsolutePath()+ ResponseUtils.URL_SEPARATOR +comment.getId())).build();
            }else {
                logger.log(Level.SEVERE,"[CommentEndPoint] addComment() user wasn't saved in session");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE,"[CommentEndPoint] addComment()",e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getByDiscoveryId(@PathParam("id") long id) {
        List<Comment> listWithComment=commentservice.getByDiscoveryId(id);
        if(listWithComment.isEmpty()) {
            logger.log(Level.SEVERE,"[CommentEndPoint] getByDiscoveryId() comment wasn't found");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return ResponseUtils.checkIfModifiedAndReturnResponse(listWithComment,requestRest).build();
    }

}
