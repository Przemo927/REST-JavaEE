package pl.przemek.service;


import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.model.VoteComment;
import pl.przemek.model.VoteType;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.repository.JpaVoteRepository;
import sun.rmi.runtime.Log;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VoteCommentService {
    private JpaUserRepository userRepo;
    private JpaCommentRepository commentRepo;
    private JpaVoteRepository<VoteComment> voteCommentRepo;
    private Comment comment;
    private User user;
    private Logger logger;

    @Inject
    public VoteCommentService(Logger logger,JpaUserRepository userRepo, JpaCommentRepository commentRepo, JpaVoteRepository<VoteComment> voteCommentRepo) {
        this.logger=logger;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.voteCommentRepo = voteCommentRepo;
    }

    public VoteCommentService() {
    }

    VoteComment createVote(VoteType voteType) {
        VoteComment vote = new VoteComment();
        vote.setUser(this.user);
        vote.setComment(this.comment);
        vote.setDate(new Timestamp((new Date()).getTime()));
        vote.setVoteType(voteType);
        return vote;
    }

    public void updateVote(long userId, long commentId, VoteType newVoteType) {
        getCommentAndUserFromDataBase(commentId,userId);
        VoteComment existingVote = null;
        VoteComment newVote = null;
        List<VoteComment> listOfVotes = this.voteCommentRepo.getVoteByUserIdVotedElementId(userId, commentId);
        if(listOfVotes.isEmpty()) {
            newVote = this.createVote(newVoteType);
            this.voteCommentRepo.add(newVote);
            this.updateComment(null, newVote);
        } else {
            existingVote = listOfVotes.get(0);
            if(!existingVote.getVoteType().equals(newVoteType)) {
                newVote = new VoteComment(existingVote);
                newVote.setVoteType(newVoteType);
                this.voteCommentRepo.update(newVote);
                this.updateComment(existingVote, newVote);
            }
        }
    }

    void updateComment(VoteComment oldVote, VoteComment updateVote) {
        if(oldVote == null && updateVote != null) {
            addVote(updateVote.getVoteType());
            this.commentRepo.update(this.comment);
        } else if(oldVote != null && updateVote != null) {
            removeVote(oldVote.getVoteType());
            addVote(updateVote.getVoteType());
            this.commentRepo.update(this.comment);
        }

    }

    void addVote(VoteType voteType) {
        if(voteType == VoteType.VOTE_UP) {
            this.comment.setUpVote(this.comment.getUpVote() + 1);
        } else if(voteType == VoteType.VOTE_DOWN) {
            this.comment.setDownVote(this.comment.getDownVote() + 1);
        }
    }

    void removeVote(VoteType voteType) {
        if(voteType == VoteType.VOTE_UP) {
            this.comment.setUpVote(this.comment.getUpVote() - 1);
        } else if(voteType == VoteType.VOTE_DOWN) {
            this.comment.setDownVote(this.comment.getDownVote() - 1);
        }
    }
    void getCommentAndUserFromDataBase(long commentId, long userId){
        this.user = this.userRepo.get(User.class, userId);
        this.comment = this.commentRepo.get(Comment.class, commentId);
        if(this.user==null) logger.log(Level.WARNING,"[VoteCommentService] getCommentAndUserFromDataBase()" +
                " user wasn't found");
        if(this.comment==null) logger.log(Level.WARNING,"[VoteCommentService] getCommentAndUserFromDataBase()" +
                " comment wasn't found");
    }
}