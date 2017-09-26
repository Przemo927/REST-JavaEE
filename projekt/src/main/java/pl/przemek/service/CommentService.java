package pl.przemek.service;


import pl.przemek.model.Comment;
import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;

import javax.inject.Inject;
import java.util.List;

public class CommentService {

    @Inject
    private JpaCommentRepository commentrepo;
    @Inject
    private JpaDiscoveryRepository discrepo;

    public void addComment(Comment comment, long id){
        Discovery discvoery=discrepo.get(id);
        comment.setDiscvovery(discvoery);
        commentrepo.add(comment);
    }
    public List<Comment> getAllComment(){
        return commentrepo.getAll();
    }

    public List<Comment> getByDiscoveryName(String name){
        return commentrepo.getByDiscoveryName(name);

    }
    public List<Comment> getByDiscoveryId(long id){
        return commentrepo.getByDiscoveryId(id);
    }
}
