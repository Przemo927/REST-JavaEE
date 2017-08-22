package pl.przemek.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@NamedQuery(name = "VoteComment.findByUserIdCommentId",query="SELECT p FROM VoteComment p WHERE p.comment.id=:commentId and p.user.id=:userId")
public class VoteComment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "vote_comment_id",nullable = false, unique = true)
    private long id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false,length=30)
    private VoteType voteType;

    public VoteComment() {
    }

    public VoteComment(VoteComment vote) {
        this.id=vote.getId();
        this.comment = vote.getComment();
        this.user = vote.getUser();
        this.date = vote.getDate();
        this.voteType = vote.getVoteType();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteComment)) return false;

        VoteComment that = (VoteComment) o;

        if (id != that.id) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return voteType == that.voteType;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (voteType != null ? voteType.hashCode() : 0);
        return result;
    }
}
