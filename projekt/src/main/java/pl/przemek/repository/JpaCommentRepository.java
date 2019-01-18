package pl.przemek.repository;


import pl.przemek.model.Comment;

import java.util.List;

public interface JpaCommentRepository {
    public void add(Comment commment);
    public void remove(Comment comment);
    public void removeByDiscoveryId(long discoveryId);
    public Comment update(Comment comment);
    public List<Comment> getAll(String nameOfQuery, Class<Comment> clazz);
    public List<Comment> getByDiscoveryName(String name);
    public List<Comment> getByDiscoveryId(long id);
    public Comment get(Class<Comment> clazz,long id);
}
