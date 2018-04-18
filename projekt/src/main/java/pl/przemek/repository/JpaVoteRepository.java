package pl.przemek.repository;

import pl.przemek.model.Vote;

import java.util.List;

public interface JpaVoteRepository {
    public void add(Vote vote);
    public Vote update(Vote vote);
    public void remove(Vote vote);
    public Vote get(Class<Vote> clazz,long id);
    public List<Vote> getAll(String nameOfQuery,Class<Vote> clazz);
    public Vote getVoteByUserIdDiscoveryId(Long UserId,Long DiscoveryId);
    public void removeByDiscoveryId(Long DiscoveryId);

}
