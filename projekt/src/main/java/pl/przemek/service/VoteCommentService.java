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
import java.util.List;

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
    public void updateVote(long userId, long commentId, VoteType newVoteType) {
        VoteComment existingVote = null;
        VoteComment newVote = null;
        List<VoteComment> listOfVotes = voteCommentRepo.getVoteByUserIdCommentId(userId, commentId);
        if (listOfVotes.isEmpty()) {
            newVote = createVote(userId, commentId, newVoteType);
            voteCommentRepo.add(newVote);
            updateComment(commentId,null,newVote);
        } else {
            existingVote=listOfVotes.get(0);
            if (!existingVote.getVoteType().equals(newVoteType)) {
                newVote=new VoteComment(existingVote);
                newVote.setVoteType(newVoteType);
                voteCommentRepo.update(newVote);
                updateComment(commentId, existingVote, newVote);
            }
       }
    }
    void updateComment(long commentId, VoteComment oldVote, VoteComment updateVote) {
        Comment comment = commentRepo.get(Comment.class,commentId);
        if (oldVote == null && updateVote!=null) {
            comment = addVote(comment, updateVote.getVoteType());
            commentRepo.update(comment);
        } else if (oldVote != null && updateVote != null) {
            comment = removeVote(comment, oldVote.getVoteType());
            comment = addVote(comment, updateVote.getVoteType());
            commentRepo.update(comment);
        }

    }

    Comment addVote(Comment comment, VoteType voteType) {
        if (voteType == VoteType.VOTE_UP) {
            comment.setUpVote(comment.getUpVote() + 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            comment.setDownVote(comment.getDownVote() + 1);
        }
        return comment;
    }

    Comment removeVote(Comment comment, VoteType voteType) {
        if (voteType == VoteType.VOTE_UP) {
            comment.setUpVote(comment.getUpVote() - 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            comment.setDownVote(comment.getDownVote() - 1);
        }
        return comment;
    }
}