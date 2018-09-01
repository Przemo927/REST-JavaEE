package pl.przemek.rest;

import pl.przemek.mapper.ExceptionMapperAnnotation;
import pl.przemek.model.User;
import pl.przemek.model.VoteType;
import pl.przemek.service.VoteCommentService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/votecom")
@ExceptionMapperAnnotation
public class VoteCommentEndPoint {


    private VoteCommentService votecommentservice;
    private HttpServletRequest request;
    private Logger logger;

    @Inject
    public VoteCommentEndPoint(Logger logger,VoteCommentService votecommentservice,HttpServletRequest request){
        this.logger=logger;
        this.votecommentservice=votecommentservice;
        this.request=request;
    }
    public VoteCommentEndPoint(){}

    @GET
    public void vote(@QueryParam("vote") String vote, @QueryParam("commentId") long commentId){
        User loggedUser=(User)request.getSession().getAttribute("user");
        if(loggedUser!=null){
            VoteType votetype=VoteType.valueOf(vote);
            Long userId=loggedUser.getId();
            votecommentservice.updateVote(userId,commentId,votetype);
        }else {
            logger.log(Level.SEVERE,"[VoteCommentEndPoint] vote() user wasn't saved in session");
        }
    }
}
