package pl.przemek.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
public class Vote implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "vote_id",nullable = false, unique = true)
    private long id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "discovery_id")
    private Discovery discovery;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	@Column(nullable = false)
    private Timestamp date;
	@Column(nullable = false,length=30)
    private VoteType voteType;
	
     
    public Vote() {}
     
    public Vote(Vote vote) {
        this.id = vote.id;
        this.discovery = new Discovery(vote.discovery);
        this.user = new User(vote.user);
        this.date = new Timestamp(vote.date.getTime());
        this.voteType= vote.voteType;
    }
     
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
   
    public Discovery getDiscovery() {
		return discovery;
	}

	public void setDiscovery(Discovery discovery) {
		this.discovery = discovery;
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
	public String toString() {
		return "Vote [id=" + id + ", discovery=" + discovery + ", user=" + user + ", date=" + date + ", voteType="
				+ voteType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((discovery == null) ? 0 : discovery.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((voteType == null) ? 0 : voteType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vote other = (Vote) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (discovery == null) {
			if (other.discovery != null)
				return false;
		} else if (!discovery.equals(other.discovery))
			return false;
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (voteType != other.voteType)
			return false;
		return true;
	}
 

}
