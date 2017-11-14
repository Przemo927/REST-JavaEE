package pl.przemek.service;


import pl.przemek.model.Comment;
import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;

import javax.inject.Inject;
import javax.validation.constraints.Null;
import java.util.List;

public class CommentService {

    private JpaCommentRepository commentRepo;
    private JpaDiscoveryRepository discRepo;

    @Inject
    public CommentService(JpaCommentRepository commentRepo,JpaDiscoveryRepository discRepo){
        this.commentRepo=commentRepo;
        this.discRepo=discRepo;
    }
    public CommentService(){
        this.commentRepo=null;
        this.discRepo=null;
    }

    public void addComment(Comment comment, long discoveryId){
        Discovery discovery=discRepo.get(discoveryId);
        if(comment!=null) {
            comment.setDiscvovery(discovery);
            commentRepo.add(comment);
        }
        else{
            throw new NullPointerException("Comment is null");
        }
    }
    public List<Comment> getAllComment(){
        return commentRepo.getAll();
    }

    public List<Comment> getByDiscoveryName(String name){
        return commentRepo.getByDiscoveryName(name);

    }
    public List<Comment> getByDiscoveryId(long id){
        return commentRepo.getByDiscoveryId(id);
    }
}
