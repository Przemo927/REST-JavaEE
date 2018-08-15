package pl.przemek.repository;

import java.util.List;

public interface JpaVoteRepository <T> {
    public void add(T element);
    public T update(T element);
    public void remove(T element);
    public T get(Class<T> clazz, long id);
    public List<T> getAll(String nameOfQuery, Class<T> clazz);
    public List<T> getVoteByUserIdVotedElementId(long userId, long elementId);
    public void removeByVotedElementId(long DiscoveryId);
}
