package pl.przemek.service;


import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.model.VoteComment;
import pl.przemek.model.VoteType;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.repository.JpaVoteCommentRepository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;

public class VoteCommentService {

    private JpaUserRepository userRepo;
    private JpaCommentRepository commentRepo;
    private JpaVoteCommentRepository voteCommentRepo;

    @Inject
    public VoteCommentService(JpaUserRepository userRepo, JpaCommentRepository commentRepo, JpaVoteCommentRepository voteCommentRepo){
        this.userRepo=userRepo;
        this.commentRepo=commentRepo;
        this.voteCommentRepo=voteCommentRepo;
    }
    public VoteCommentService(){
        this.userRepo=null;
        this.commentRepo=null;
        this.voteCommentRepo=null;
    }


    VoteComment createVote(long userId, long commentId, VoteType voteType) {
        User user = userRepo.get(User.class,userId);
        Comment comment = commentRepo.get(Comment.class,commentId);
        if(user!=null && comment!=null) {
            VoteComment vote = new VoteComment();
            vote.setComment(comment);
            vote.setDate(new Timestamp(new Date().getTime()));
            vote.setUser(user);
            vote.setVoteType(voteType);
            return vote;
        }
        return null;
    }
    VoteComment createVoteFromExistingVote(VoteComment voteComment) {
        VoteComment vote=new VoteComment(voteComment);
        return vote;
    }
    Comment createComment(Comment comment){
        Comment newComment=new Comment(comment);
        return newComment;
    }

    public void updateVote(long userId, long commentId, VoteType newVoteType) {
        VoteComment updateVote = null;
        VoteComment existingVote = null;
        existingVote = voteCommentRepo.getVoteByUserIdCommentId(userId, commentId);
        if (existingVote == null) {
            updateVote = createVote(userId, commentId, newVoteType);
            voteCommentRepo.add(updateVote);
            updateComment(commentId,null,updateVote);
        } else {
            updateVote = createVoteFromExistingVote(existingVote);
            updateVote.setVoteType(newVoteType);
            if (!existingVote.equals(updateVote)) {
                voteCommentRepo.update(updateVote);
                updateComment(commentId, existingVote, updateVote);
            }
       }
    }
    void updateComment(long commentId, VoteComment oldVote, VoteComment updateVote) {
        Comment comment = commentRepo.get(Comment.class,commentId);
        Comment updateComment=null;
        if (oldVote == null && updateVote!=null) {
            updateComment = addCommentVote(comment, updateVote.getVoteType());
            commentRepo.update(updateComment);
        } else if (oldVote != null && updateVote != null) {
            Comment commentWithRemovedVote = removeCommentVote(comment, oldVote.getVoteType());
            updateComment = addCommentVote(commentWithRemovedVote, updateVote.getVoteType());
            commentRepo.update(updateComment);
        }

    }

    Comment addCommentVote(Comment comment, VoteType voteType) {
        Comment copyComment = createComment(comment);
        if (voteType == VoteType.VOTE_UP) {
            copyComment.setUpVote(copyComment.getUpVote() + 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyComment.setDownVote(copyComment.getDownVote() + 1);
        }
        return copyComment;
    }

    Comment removeCommentVote(Comment comment, VoteType voteType) {
        Comment copyComment = createComment(comment);
        if (voteType == VoteType.VOTE_UP) {
            copyComment.setUpVote(copyComment.getUpVote() - 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyComment.setDownVote(copyComment.getDownVote() - 1);
        }
        return copyComment;
    }
}