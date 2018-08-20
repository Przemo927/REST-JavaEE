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
        try {
            Discovery discovery = discRepo.get(Discovery.class, discoveryId);
            if (comment == null)
                logger.log(Level.WARNING, "[CommentService] addComment() comment is null");
            else if (discovery == null)
                logger.log(Level.WARNING, "[CommentService] addComment() discovery is null");
            else {
                comment.setDiscovery(discovery);
                commentRepo.add(comment);
            }
        }catch (Exception e){
            logger.log(Level.SEVERE,"[CommentService] addComment()",e);
        }
    }
    public List<Comment> getAllComment(){
        List<Comment> listOfComment=null;
        try{
            listOfComment=commentRepo.getAll("Comment.findAll",Comment.class);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[CommentService] getAllComments()",e);
            return Collections.emptyList();
        }
        return listOfComment;
    }

    public List<Comment> getByDiscoveryName(String name){
        List<Comment> listOfComment=null;
        try {
            listOfComment=commentRepo.getByDiscoveryName(name);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[CommentService] getByDiscoveryName()",e);
            return Collections.emptyList();
        }
        return listOfComment;
    }
    public List<Comment> getByDiscoveryId(long id){
        List<Comment> listOfComment=null;
        try {
            listOfComment=commentRepo.getByDiscoveryId(id);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[CommentService] getByDiscoveryId()",e);
            return Collections.emptyList();
        }
        return listOfComment;
    }
}
