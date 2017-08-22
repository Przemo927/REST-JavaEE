package pl.przemek.service;


import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.model.VoteComment;
import pl.przemek.model.VoteType;
import pl.przemek.repository.CommentRepository;
import pl.przemek.repository.UserRepository;
import pl.przemek.repository.VoteCommentRepository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;

public class VoteCommentService {

    @Inject
    UserRepository userrepo;
    @Inject
    CommentRepository commentrepo;
    @Inject
    VoteCommentRepository votecommentrepo;

    private VoteComment CreateVote(long userId, long commentId, VoteType voteType) {
        User user = userrepo.get(userId);
        Comment comment = commentrepo.get(commentId);
        VoteComment vote = new VoteComment();
        vote.setComment(comment);
        vote.setDate(new Timestamp(new Date().getTime()));
        vote.setUser(user);
        vote.setVoteType(voteType);
        return vote;
    }

    public void updateVote(long userId, long commentId, VoteType newVoteType) {
        VoteComment updateVote = null;
        VoteComment existingVote = null;
        existingVote = votecommentrepo.getVoteByUserIdCommentId(userId, commentId);
        //System.out.println(existingVote.getVoteType());
        if (existingVote == null) {
            updateVote = CreateVote(userId, commentId, newVoteType);
            votecommentrepo.add(updateVote);
            updateComment(commentId,null,updateVote);
        } else {
            VoteComment oldVote = new VoteComment(existingVote);
            existingVote.setVoteType(newVoteType);
            updateVote = votecommentrepo.update(existingVote);
            if (oldVote != updateVote || !updateVote.equals(oldVote)) {
                updateComment(commentId, oldVote, updateVote);
            }
       }
    }
    private void updateComment(long commentId, VoteComment oldVote, VoteComment updateVote) {
        Comment comment = commentrepo.get(commentId);
        Comment updateComment=null;
        if (oldVote == null && updateVote != null) {
            updateComment = AddCommentVote(comment, updateVote.getVoteType());
            commentrepo.update(updateComment);
        } else if (oldVote != null && updateVote != null) {
            Comment commentWithRemovedVote = removeDiscoveryVote(comment, oldVote.getVoteType());
            updateComment = AddCommentVote(commentWithRemovedVote, updateVote.getVoteType());
            commentrepo.update(updateComment);
        }

    }

    private Comment AddCommentVote(Comment comment, VoteType voteType) {
        Comment copyComment = new Comment(comment);
        if (voteType == VoteType.VOTE_UP) {
            copyComment.setUpVote(copyComment.getUpVote() + 1);
            System.out.println("Nowy up +1");
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyComment.setDownVote(copyComment.getDownVote() + 1);
            System.out.println("Nowy up +1");
        }
        return copyComment;
    }

    private Comment removeDiscoveryVote(Comment comment, VoteType voteType) {
        Comment copyComment = new Comment(comment);
        if (voteType == VoteType.VOTE_UP) {
            copyComment.setUpVote(copyComment.getUpVote() - 1);
        } else if (voteType == VoteType.VOTE_DOWN) {
            copyComment.setDownVote(copyComment.getDownVote() - 1);
        }
        return copyComment;
    }
}