package pl.przemek.rest;

import pl.przemek.model.User;
import pl.przemek.model.VoteType;
import pl.przemek.service.VoteCommentService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/votecom")
public class VoteCommentEndPoint {


    VoteCommentService votecommentservice;
    HttpServletRequest request;

    @Inject
    public VoteCommentEndPoint(VoteCommentService votecommentservice,HttpServletRequest request){
        this.votecommentservice=votecommentservice;
        this.request=request;
    }
    public VoteCommentEndPoint(){}

    @GET
    public void voting(@QueryParam("vote") String vote, @QueryParam("commentId") long commentId){
        User loggedUser=(User)request.getSession().getAttribute("user");
        if(loggedUser!=null){
            VoteType votetype=VoteType.valueOf(vote);
            Long userId=loggedUser.getId();
            votecommentservice.updateVote(loggedUser.getId(),commentId,votetype);
        }
    }
}
