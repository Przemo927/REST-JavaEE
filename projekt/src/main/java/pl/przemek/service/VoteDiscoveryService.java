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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VoteDiscoveryService {
    private JpaVoteRepository<Vote> votRepo;
    private JpaDiscoveryRepository disRepo;
    private JpaUserRepository userRepo;
    private Logger logger;
    private Discovery discovery;
    private User user;

    @Inject
    public VoteDiscoveryService(Logger logger,JpaVoteRepository<Vote> votRepo, JpaDiscoveryRepository disRepo, JpaUserRepository userRepo) {
        this.votRepo = votRepo;
        this.disRepo = disRepo;
        this.userRepo = userRepo;
        this.logger=logger;
    }

    public VoteDiscoveryService() {
    }

    Vote createVote( VoteType votetype) {
        Vote vote = new Vote();
        vote.setUser(this.user);
        vote.setDiscovery(this.discovery);
        vote.setDate(new Timestamp((new Date()).getTime()));
        vote.setVoteType(votetype);
        return vote;
    }

    public void updateVote(long userId, long discoveryId, VoteType newVoteType) {
        getDiscoveryAndUserFromDataBase(discoveryId,userId);
        Vote newVote = null;
        Vote existingVote = null;
        List<Vote> listOfVotes = this.votRepo.getVoteByUserIdVotedElementId(userId, discoveryId);
        if(listOfVotes.isEmpty()) {
            newVote = this.createVote(newVoteType);
            this.votRepo.add(newVote);
            this.updateDiscovery(discoveryId, (Vote)null, newVote);
        } else {
            existingVote = (Vote)listOfVotes.get(0);
            if(!existingVote.getVoteType().equals(newVoteType)) {
                newVote = new Vote(existingVote);
                newVote.setVoteType(newVoteType);
                this.votRepo.update(newVote);
                this.updateDiscovery(discoveryId, existingVote, newVote);
            }
        }
    }

    void updateDiscovery(long discoveryId, Vote oldVote, Vote updateVote) {
        if(oldVote == null && updateVote != null) {
            addDiscoveryVote(updateVote.getVoteType());
            this.disRepo.update(this.discovery);
        } else if(oldVote != null && updateVote != null) {
            removeDiscoveryVote(oldVote.getVoteType());
            addDiscoveryVote(updateVote.getVoteType());
            this.disRepo.update(this.discovery);
        }

    }

    void addDiscoveryVote(VoteType voteType) {
        if(voteType == VoteType.VOTE_UP) {
            this.discovery.setUpVote(this.discovery.getUpVote() + 1);
        } else if(voteType == VoteType.VOTE_DOWN) {
            this.discovery.setDownVote(this.discovery.getDownVote() + 1);
        }
    }

    void removeDiscoveryVote(VoteType voteType) {
        if(voteType == VoteType.VOTE_UP) {
            this.discovery.setUpVote(this.discovery.getUpVote() - 1);
        } else if(voteType == VoteType.VOTE_DOWN) {
            this.discovery.setDownVote(this.discovery.getDownVote() - 1);
        }
    }

    public Optional<Vote> getById(long id) {
        Vote vote=this.votRepo.get(Vote.class, id);
        return Optional.ofNullable(vote);
    }

    public List<Vote> getByUserIdDiscoveryId(long userId, long discoveryId) {
        return this.votRepo.getVoteByUserIdVotedElementId(userId, discoveryId);
    }

    public void removeByDiscveryId(Long discoveryid) {
        this.votRepo.removeByVotedElementId(discoveryid);
    }

    void getDiscoveryAndUserFromDataBase(long discoveryId, long userId){
        this.user = this.userRepo.get(User.class, userId);
        this.discovery = this.disRepo.get(Discovery.class, discoveryId);
        if(this.user==null) logger.log(Level.WARNING,"[VoteDiscoveryService] getDiscoveryAndUserFromDataBase()" +
                " user wasn't found");
        if(this.discovery==null) logger.log(Level.WARNING,"[VoteDiscoveryService] getDiscoveryAndUserFromDataBase()" +
                " discovery wasn't found");
    }
}
