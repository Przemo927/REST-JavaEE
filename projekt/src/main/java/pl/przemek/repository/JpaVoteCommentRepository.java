package pl.przemek.repository;


import pl.przemek.model.VoteComment;

public interface JpaVoteCommentRepository {
    public void add(VoteComment vote);
    public VoteComment update(VoteComment vote);
    public void remove(VoteComment vote);
    public VoteComment get(Class<VoteComment> clazz,long id);
    public VoteComment getVoteByUserIdCommentId(Long UserId, Long CommentId);
    public void removeByCommentId(Long CommentId);

}
