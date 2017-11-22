package pl.przemek.service;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.model.Vote;
import pl.przemek.model.VoteType;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.repository.JpaVoteRepository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;


public class VoteService {

    private JpaVoteRepository votRepo;
    private JpaDiscoveryRepository disRepo;
    private JpaUserRepository userRepo;

    @Inject
    public VoteService(JpaVoteRepository votRepo,JpaDiscoveryRepository disRepo,JpaUserRepository userRepo){
        this.votRepo=votRepo;
        this.disRepo=disRepo;
        this.userRepo=userRepo;
    }
    public VoteService(){
        this.votRepo=null;
        this.disRepo=null;
        this.userRepo=null;
    }

    Vote createVote(long userId, long discoveryId, VoteType votetype) {
        User user = userRepo.get(userId);
        Discovery discovery = disRepo.get(discoveryId);
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setDiscovery(discovery);
        vote.setDate(new Timestamp(new Date().getTime()));
        vote.setVoteType(votetype);
        return vote;
    }
    Vote createVote(Vote vote){
        Vote newVote=new Vote(vote);
        return  newVote;
    }
    Discovery createDicovery(Discovery discovery){
        Discovery newDiscovery=new Discovery(discovery);
        return newDiscovery;
    }

    public void updateVote(long userId, long discoveryId, VoteType newVoteType) {
        Vote updateVote = null;
        Vote existingVote = null;
        existingVote = votRepo.getVoteByUserIdDiscoveryId(userId, discoveryId);
        System.out.println(existingVote);
        if (existingVote == null) {
            updateVote = createVote(userId, discoveryId, newVoteType);
            votRepo.add(updateVote);
            updateDiscovery(discoveryId,null,updateVote);
        } else {
            updateVote = createVote(existingVote);
            updateVote.setVoteType(newVoteType);
            if (!updateVote.equals(existingVote)) {
                votRepo.update(updateVote);
                updateDiscovery(discoveryId, existingVote, updateVote);
            }
        }

    }

    void updateDiscovery(long discoveryId, Vote oldVote, Vote updateVote) {
        Discovery discovery = disRepo.get(discoveryId);
        Discovery updateDiscovery=null;
        if (oldVote == null && updateVote != null) {
            updateDiscovery = addDiscoveryVote(discovery, updateVote.getVoteType());
            disRepo.update(updateDiscovery);
        } else if (oldVote != null && updateVote != null) {
            Discovery discoveryWithRemovedVote = removeDiscoveryVote(discovery, oldVote.getVoteType());
            updateDiscovery = addDiscoveryVote(discoveryWithRemovedVote, updateVote.getVoteType());
            disRepo.update(updateDiscovery);
        }

    }

    Discovery addDiscoveryVote(Discovery discovery, VoteType voteType) {
        Discovery copyDiscovery = createDicovery(discovery);
        if (voteType == VoteType.VOTE_UP) {
            copyDiscovery.setUpVote(copyDiscovery.getUpVote() + 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyDiscovery.setDownVote(copyDiscovery.getDownVote() + 1);
        }
        return copyDiscovery;
    }

    Discovery removeDiscoveryVote(Discovery discovery, VoteType voteType) {
        Discovery copyDiscovery = createDicovery(discovery);
        if (voteType == VoteType.VOTE_UP) {
            copyDiscovery.setUpVote(copyDiscovery.getUpVote() - 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyDiscovery.setDownVote(copyDiscovery.getDownVote() - 1);
        }
        return copyDiscovery;
    }

    public Vote getById(Long id) {
        Vote vote = votRepo.get(id);
        return vote;
    }

    public Vote getByUserIdDiscoveryId(Long userId, Long discoveryId) {
        Vote vote = votRepo.getVoteByUserIdDiscoveryId(userId, discoveryId);
        return vote;
    }
    public void removeByDiscveryId(Long discoveryid) {
        votRepo.removeByDiscoveryId(discoveryid);
    }
}
