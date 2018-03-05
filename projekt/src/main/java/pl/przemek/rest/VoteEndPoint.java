
package pl.przemek.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.model.Vote;
import pl.przemek.model.VoteType;
import pl.przemek.service.VoteService;
import pl.przemek.wrapper.ResponseMessageWrapper;

@Path("/vote")
public class VoteEndPoint {

private VoteService voteService;

private HttpServletRequest request;
	private final static ResponseMessageWrapper mw=new ResponseMessageWrapper();


@Inject
public VoteEndPoint(VoteService voteService,HttpServletRequest request){
	this.voteService=voteService;
	this.request=request;
}
public VoteEndPoint(){}
@GET
public void voting(@QueryParam("vote") String vote, @QueryParam("discoveryId") Long discoveryId) throws URISyntaxException {
	User loggedUser = (User) request.getSession().getAttribute("user");

	if(loggedUser!=null){
		VoteType votetype=VoteType.valueOf(vote);
		Long userId=loggedUser.getId();
		voteService.updateVote(userId,discoveryId,votetype);
	}
}
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{id}")
public Response get(@PathParam("id") long id){
	Vote vote=voteService.getById(id);
	if(vote==null)
		return Response.status(Response.Status.NO_CONTENT).build();
	return Response.ok(vote).build();
}
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{id}/{id1}")
public Response getVoteByUserIdDiscoveryId(@PathParam("id") long userId, @PathParam("id1") long discoveryId){
	Vote vote=voteService.getByUserIdDiscoveryId(userId,discoveryId);
	if(vote==null)
		return Response.status(Response.Status.NO_CONTENT).build();
	return Response.ok(vote).build();
	
}
}
