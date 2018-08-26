package pl.przemek.service;


import pl.przemek.model.Comment;
import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;

import javax.inject.Inject;
import java.util.Collections;
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
    public CommentService(){
    }

    public void addComment(Comment comment, long discoveryId){
        Discovery discovery = discRepo.get(Discovery.class, discoveryId);
        if (comment == null)
            logger.log(Level.WARNING, "[CommentService] addComment() comment is null");
        else if (discovery == null)
            logger.log(Level.WARNING, "[CommentService] addComment() discovery is null");
        else {
            comment.setDiscovery(discovery);
            commentRepo.add(comment);
        }
    }
    public List<Comment> getAllComment(){
        return commentRepo.getAll("Comment.findAll",Comment.class);
    }

    public List<Comment> getByDiscoveryName(String name){
        return commentRepo.getByDiscoveryName(name);
    }
    public List<Comment> getByDiscoveryId(long id){
        return commentRepo.getByDiscoveryId(id);
    }
}
