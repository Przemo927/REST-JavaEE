package pl.przemek.service;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.model.Vote;
import pl.przemek.model.VoteType;
import pl.przemek.repository.DiscoveryRepository;
import pl.przemek.repository.UserRepository;
import pl.przemek.repository.VoteRepository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;


public class VoteService {

    @Inject
    VoteRepository votrepo;
    @Inject
    DiscoveryRepository disrepo;
    @Inject
    UserRepository userrepo;

    private Vote CreateVote(long userId, long discoveryId, VoteType votetype) {
        User user = userrepo.get(userId);
        Discovery discovery = disrepo.get(discoveryId);
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setDiscovery(discovery);
        vote.setDate(new Timestamp(new Date().getTime()));
        vote.setVoteType(votetype);
        return vote;
    }

    public void updateVote(long userId, long discovery_id, VoteType newVoteType) {
        Vote updateVote = null;
        Vote existingVote = null;
        existingVote = votrepo.getVoteByUserIdDiscoveryId(userId, discovery_id);
        if (existingVote == null) {
            updateVote = CreateVote(userId, discovery_id, newVoteType);
            votrepo.add(updateVote);
            updateDiscovery(discovery_id,null,updateVote);
        } else {
            Vote oldVote = new Vote(existingVote);
            existingVote.setVoteType(newVoteType);
            updateVote = votrepo.update(existingVote);
            if (oldVote != updateVote || !updateVote.equals(oldVote)) {
                updateDiscovery(discovery_id, oldVote, updateVote);
            }
        }

    }

    private void updateDiscovery(long discoveryId, Vote oldVote, Vote updateVote) {
        Discovery discovery = disrepo.get(discoveryId);
        Discovery updateDiscovery=null;
        if (oldVote == null && updateVote != null) {
            updateDiscovery = AddDiscoveryVote(discovery, updateVote.getVoteType());
            disrepo.update(updateDiscovery);
        } else if (oldVote != null && updateVote != null) {
            Discovery discoveryWithRemovedVote = removeDiscoveryVote(discovery, oldVote.getVoteType());
            updateDiscovery = AddDiscoveryVote(discoveryWithRemovedVote, updateVote.getVoteType());
            disrepo.update(updateDiscovery);
        }

    }

    private Discovery AddDiscoveryVote(Discovery discovery, VoteType voteType) {
        Discovery copyDiscovery = new Discovery(discovery);
        if (voteType == VoteType.VOTE_UP) {
            copyDiscovery.setUpVote(copyDiscovery.getUpVote() + 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyDiscovery.setDownVote(copyDiscovery.getDownVote() + 1);
        }
        return copyDiscovery;
    }

    private Discovery removeDiscoveryVote(Discovery discovery, VoteType voteType) {
        Discovery copyDiscovery = new Discovery(discovery);
        if (voteType == VoteType.VOTE_UP) {
            copyDiscovery.setUpVote(copyDiscovery.getUpVote() - 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyDiscovery.setDownVote(copyDiscovery.getDownVote() - 1);
        }
        return copyDiscovery;
    }

    public Vote getById(Long id) {
        Vote vote = votrepo.get(id);
        return vote;
    }

    public Vote getByUserIdDiscoveryId(Long userId, Long discoveryId) {
        Vote vote = votrepo.getVoteByUserIdDiscoveryId(userId, discoveryId);
        return vote;
    }
    public void removeByDiscveryId(Long discoveryid) {
        votrepo.removeByDiscoveryId(discoveryid);
    }
}
