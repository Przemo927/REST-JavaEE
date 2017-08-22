
package pl.przemek.rest;

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

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.model.Vote;
import pl.przemek.model.VoteType;
import pl.przemek.repository.DiscoveryRepository;
import pl.przemek.repository.UserRepository;
import pl.przemek.repository.VoteRepository;
import pl.przemek.service.VoteService;

@Path("/vote")
public class VoteEndPoint {

@Inject
private VoteService voteService;
@Inject
HttpServletRequest request;

@GET
public void Voting(@QueryParam("vote") String vote,@QueryParam("discovery_id") Long discovery_id){
	User loggedUser = (User) request.getSession().getAttribute("user");

	if(loggedUser!=null){
		VoteType votetype=VoteType.valueOf(vote);
		Long user_id=loggedUser.getId();
		voteService.updateVote(user_id,discovery_id,votetype);
	}
}
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{id}")
public Vote get(@PathParam("id") long id){
	Vote vote=voteService.getById(id);
	return vote;
	
}
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{id}/{id1}")
public Vote get(@PathParam("id") long userId,@PathParam("id1") long discoveryId){
	Vote vote=voteService.getByUserIdDiscoveryId(userId,discoveryId);
	return vote;
	
}
}
