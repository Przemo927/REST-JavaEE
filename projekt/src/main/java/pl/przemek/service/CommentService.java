package pl.przemek.service;


import pl.przemek.model.Comment;
import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentService {

    private JpaCommentRepository commentRepo;
    private JpaDiscoveryRepository discRepo;
    private Logger logger;

    @Inject
    public CommentService(Logger logger,JpaCommentRepository commentRepo, JpaDiscoveryRepository discRepo){
        this.logger=logger;
        this.commentRepo=commentRepo;
        this.discRepo=discRepo;
    }

    public void addComment(Comment comment){
        if (comment == null)
            logger.log(Level.WARNING, "[CommentService] addComment() comment is null");
        else {
            Discovery discovery=comment.getDiscovery();
            discovery.getComments().add(comment);
            discRepo.update(discovery);
        }
    }
    public List<Comment> getAllComment(){
        return commentRepo.getAll("Comment.findAll",Comment.class);
    }

    List<Comment> getByDiscoveryName(String name){
        return commentRepo.getByDiscoveryName(name);
    }
    public List<Comment> getByDiscoveryId(long id){
        return commentRepo.getByDiscoveryId(id);
    }
}
