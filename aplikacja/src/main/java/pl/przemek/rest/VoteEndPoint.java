package pl.przemek.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
	Vote existingVote=votrepo.getVoteByUserIdDiscoveryId(user_id, discovery_id);
	Vote oldVote=new Vote(existingVote);
	if(existingVote==null){
		updateVote=CreateVote(user_id,discovery_id,newVoteType);
		votrepo.add(updateVote);
		}
	else{
		existingVote.setVoteType(newVoteType);
		updateVote=votrepo.update(existingVote);
	}
	if(oldVote!=updateVote || !updateVote.equals(oldVote)){
		updateDiscovery(discovery_id,oldVote,updateVote);
	}
	
	}
private void updateDiscovery(long discovery_id,Vote oldVote, Vote updateVote){
	Discovery discovery=disrepo.get(discovery_id);
	if(oldVote==null && updateVote!=null){
		AddDiscoveryVote(discovery,updateVote.getVoteType());
	}
	else if(oldVote!=null && updateVote!=null){
		removeDiscoveryVote(discovery,updateVote.getVoteType());
		AddDiscoveryVote(discovery,updateVote.getVoteType());
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
}
