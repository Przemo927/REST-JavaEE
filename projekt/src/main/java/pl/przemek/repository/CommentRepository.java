package pl.przemek.repository;


import pl.przemek.model.Comment;

import java.util.List;

public interface CommentRepository {
    public void add(Comment commment);
    public void remove(Comment comment);
    public Comment update(Comment comment);
    public List<Comment> getAll();
    public List<Comment> getByDiscoveryName(String name);
    public List<Comment> getByDiscoveryId(long id);
    public Comment get(long id);
}
