package pl.przemek.repository;


import pl.przemek.model.Vote;
import pl.przemek.model.VoteComment;

import java.util.List;

public interface VoteCommentRepository {
    public void add(VoteComment vote);
    public VoteComment update(VoteComment vote);
    public void remove(VoteComment vote);
    public VoteComment get(Long id);
    public VoteComment getVoteByUserIdCommentId(Long UserId,Long CommentId);
    public void removeByCommentId(Long CommentId);

}
