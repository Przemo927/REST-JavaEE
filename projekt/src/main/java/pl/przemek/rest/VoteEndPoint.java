
package pl.przemek.rest;

import pl.przemek.mapper.ExceptionMapperAnnotation;
import pl.przemek.model.User;
import pl.przemek.model.Vote;
import pl.przemek.model.VoteType;
import pl.przemek.service.VoteDiscoveryService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/vote")
@ExceptionMapperAnnotation
public class VoteEndPoint {

	private VoteDiscoveryService voteService;
	private HttpServletRequest request;
	private Logger logger;

	@Inject
	public VoteEndPoint(Logger logger,VoteDiscoveryService voteService,HttpServletRequest request){
		this.logger=logger;
		this.voteService=voteService;
		this.request=request;
	}
	public VoteEndPoint(){}
	@GET
	public void vote(@QueryParam("vote") String vote, @QueryParam("discoveryId") Long discoveryId) throws URISyntaxException {
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
		Optional<Vote> voteOptional=voteService.getById(id);
		return voteOptional.map(vote -> {
			return Response.ok(vote).build();
		}).orElseGet(()->{
			logger.log(Level.SEVERE,"[VoteEndPoint] get() vote wasn't found");
			return Response.status(Response.Status.NO_CONTENT).build();
		});
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/{id1}")
	public Response getVoteByUserIdDiscoveryId(@PathParam("id") long userId, @PathParam("id1") long discoveryId){
		List<Vote> voteList=voteService.getByUserIdDiscoveryId(userId,discoveryId);
		if(voteList.isEmpty()) {
			logger.log(Level.SEVERE,"[VoteEndPoint] getVoteByUserIdDiscoveryId() vote wasn't found");
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		return Response.ok(voteList.get(0)).build();
	}
}
