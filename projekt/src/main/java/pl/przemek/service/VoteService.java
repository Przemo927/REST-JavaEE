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

    private Vote CreateVote(long user_id, long discovery_id, VoteType votetype) {
        User user = userrepo.get(user_id);
        Discovery discovery = disrepo.get(discovery_id);
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setDiscovery(discovery);
        vote.setDate(new Timestamp(new Date().getTime()));
        vote.setVoteType(votetype);
        return vote;
    }

    public void updateVote(long user_id, long discovery_id, VoteType newVoteType) {
        Vote updateVote = null;
        Vote existingVote = null;
        existingVote = votrepo.getVoteByUserIdDiscoveryId(user_id, discovery_id);
        if (existingVote == null) {
            updateVote = CreateVote(user_id, discovery_id, newVoteType);
            votrepo.add(updateVote);
        } else {
            Vote oldVote = new Vote(existingVote);
            existingVote.setVoteType(newVoteType);
            updateVote = votrepo.update(existingVote);
            if (oldVote != updateVote || !updateVote.equals(oldVote)) {
                updateDiscovery(discovery_id, oldVote, updateVote);
            }
        }

    }

    private void updateDiscovery(long discovery_id, Vote oldVote, Vote updateVote) {
        Discovery discovery = disrepo.get(discovery_id);
        if (oldVote == null && updateVote != null) {
            Discovery newdiscovery = AddDiscoveryVote(discovery, updateVote.getVoteType());
            disrepo.update(newdiscovery);
        } else if (oldVote != null && updateVote != null) {
            Discovery ndiscovery = removeDiscoveryVote(discovery, oldVote.getVoteType());
            Discovery newdiscovery = AddDiscoveryVote(ndiscovery, updateVote.getVoteType());
            disrepo.update(newdiscovery);
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
