
package pl.przemek.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

@Path("/vote")
public class VoteEndPoint {

@Inject
private VoteRepository votrepo;
@Inject
private HttpServletRequest request;
@Inject
private UserRepository userepo;
@Inject
private DiscoveryRepository disrepo;

private Vote CreateVote(long user_id, long discovery_id,VoteType votetype){
	User user=userepo.get(user_id);
	Discovery discovery=disrepo.get(discovery_id);
	Vote vote=new Vote();
	vote.setUser(user);
	vote.setDiscovery(discovery);
	vote.setDate(new Timestamp(new Date().getTime()));
	vote.setVoteType(votetype);
	return vote;
	
}
@GET
public void Voting(@QueryParam("vote") String vote,@QueryParam("discovery_id") Long discovery_id){
	User loggedUser = (User) request.getSession().getAttribute("user");

	if(loggedUser!=null){
		VoteType votetype=VoteType.valueOf(vote);
		Long user_id=loggedUser.getId();
		updateVote(user_id,discovery_id,votetype);
	}
}
	private void updateVote(long user_id,long discovery_id,VoteType newVoteType){
		Vote updateVote=null;
		Vote existingVote=null;
	existingVote=votrepo.getVoteByUserIdDiscoveryId(user_id, discovery_id);
	if(existingVote==null){
		updateVote=CreateVote(user_id,discovery_id,newVoteType);
		votrepo.add(updateVote);
		}
	else{
		Vote oldVote=new Vote(existingVote);
		existingVote.setVoteType(newVoteType);
		updateVote=votrepo.update(existingVote);
	if(oldVote!=updateVote || !updateVote.equals(oldVote)){
		updateDiscovery(discovery_id,oldVote,updateVote);
	}}
	
	}
private void updateDiscovery(long discovery_id,Vote oldVote, Vote updateVote){
	Discovery discovery=disrepo.get(discovery_id);
	if(oldVote==null && updateVote!=null){
		Discovery newdiscovery=AddDiscoveryVote(discovery,updateVote.getVoteType());
		disrepo.update(newdiscovery);
	}
	else if(oldVote!=null && updateVote!=null){
		Discovery ndiscovery=removeDiscoveryVote(discovery,oldVote.getVoteType());
		Discovery newdiscovery=AddDiscoveryVote(ndiscovery,updateVote.getVoteType());
		disrepo.update(newdiscovery);
	}
	
}
private Discovery AddDiscoveryVote(Discovery discovery, VoteType voteType){
	Discovery copyDiscovery=new Discovery(discovery);
	 if(voteType == VoteType.VOTE_UP) {
         copyDiscovery.setUpVote(copyDiscovery.getUpVote() + 1);
     } else if(voteType == VoteType.VOTE_DOWN) {
    	 copyDiscovery.setDownVote(copyDiscovery.getDownVote() + 1);
     }
     return copyDiscovery;
}
private Discovery removeDiscoveryVote(Discovery discovery, VoteType voteType) {
    Discovery copyDiscovery = new Discovery(discovery);
    if(voteType == VoteType.VOTE_UP) {
    	copyDiscovery.setUpVote(copyDiscovery.getUpVote() - 1);
    } else if(voteType == VoteType.VOTE_DOWN) {
    	copyDiscovery.setDownVote(copyDiscovery.getDownVote() - 1);
    }
    return copyDiscovery;
}
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{id}")
public Vote get(@PathParam("id") long id){
	Vote vote=votrepo.get(id);
	return vote;
	
}
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{id}/{id1}")
public Vote get(@PathParam("id") long id,@PathParam("id1") long id1){
	Vote vote=votrepo.getVoteByUserIdDiscoveryId(id, id1);
	return vote;
	
}
}
