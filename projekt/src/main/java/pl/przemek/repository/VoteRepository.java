package pl.przemek.repository;

import pl.przemek.model.Vote;

import java.util.List;

public interface VoteRepository {
    public Vote update(Vote vote);
    public void remove(Vote vote);
    public Vote get(Long id);
    public List<Vote> getAll();
    public Vote getVoteByUserIdDiscoveryId(Long UserId,Long DiscoveryId);

}
