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
import java.util.List;
import java.util.Optional;


public class VoteService {

    private JpaVoteRepository votRepo;
    private JpaDiscoveryRepository disRepo;
    private JpaUserRepository userRepo;

    @Inject
    public VoteService(JpaVoteRepository votRepo, JpaDiscoveryRepository disRepo, JpaUserRepository userRepo){
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
        User user = userRepo.get(User.class,userId);
        Discovery discovery = disRepo.get(Discovery.class,discoveryId);
        if(discovery!=null && user!=null) {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setDiscovery(discovery);
            vote.setDate(new Timestamp(new Date().getTime()));
            vote.setVoteType(votetype);
            return vote;
        }
        return null;
    }


    public void updateVote(long userId, long discoveryId, VoteType newVoteType) {
        Vote newVote = null;
        Vote existingVote = null;
        List<Vote> listOfVotes = votRepo.getVoteByUserIdDiscoveryId(userId, discoveryId);
        if (listOfVotes.isEmpty()) {
            newVote = createVote(userId, discoveryId, newVoteType);
            votRepo.add(newVote);
            updateDiscovery(discoveryId,null,newVote);
        } else {
            existingVote=listOfVotes.get(0);
            if (!existingVote.getVoteType().equals(newVoteType)) {
                newVote = new Vote(existingVote);
                newVote.setVoteType(newVoteType);
                votRepo.update(newVote);
                updateDiscovery(discoveryId, existingVote, newVote);
            }
        }

    }

    void updateDiscovery(long discoveryId, Vote oldVote, Vote updateVote) {
        Discovery discovery = disRepo.get(Discovery.class,discoveryId);
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
        if (voteType == VoteType.VOTE_UP) {
            discovery.setUpVote(discovery.getUpVote() + 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            discovery.setDownVote(discovery.getDownVote() + 1);
        }
        return discovery;
    }

    Discovery removeDiscoveryVote(Discovery discovery, VoteType voteType) {
        if (voteType == VoteType.VOTE_UP) {
            discovery.setUpVote(discovery.getUpVote() - 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            discovery.setDownVote(discovery.getDownVote() - 1);
        }
        return discovery;
    }

    public Optional<Vote> getById(Long id) {
        Vote vote = votRepo.get(Vote.class,id);
        return Optional.ofNullable(vote);
    }

    public Optional<Vote> getByUserIdDiscoveryId(Long userId, Long discoveryId) {
        List<Vote> listOfVotes = votRepo.getVoteByUserIdDiscoveryId(userId, discoveryId);
        if(listOfVotes.isEmpty())
            return Optional.empty();
        return Optional.of(listOfVotes.get(0));
    }
    public void removeByDiscveryId(Long discoveryid) {
        votRepo.removeByDiscoveryId(discoveryid);
    }
}
