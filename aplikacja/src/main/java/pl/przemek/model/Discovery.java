package pl.przemek.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
@Entity
    @NamedQuery(name = "Discovery.findAll", query = "SELECT p FROM Discovery p")
public class Discovery implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "discovery_id",nullable = false, unique = true)
	private long id;
	@Column(nullable = false, length=100)
    private String name;
	@Column(nullable = false, length=250)
    private String description;
	@Column(nullable = false, length=200, unique = true)
    private String url;
	@Column(nullable = false)
    private Timestamp timestamp;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private int upVote;
    @Column(nullable = false)
    private int downVote;
     
    public Discovery(){}
     
    public Discovery(Discovery discovery) {
        this.id = discovery.id;
        this.name = discovery.name;
        this.description = discovery.description;
        this.url = discovery.url;
        this.timestamp = new Timestamp(discovery.timestamp.getTime());
        this.user = new User(discovery.user);
        this.upVote = discovery.upVote;
        this.downVote = discovery.downVote;
    }
     
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public int getUpVote() {
        return upVote;
    }
    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }
    public int getDownVote() {
        return downVote;
    }
    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }
 
    @Override
    public String toString() {
        return "Discovery [id=" + id + ", name=" + name + ", description=" + description + ", url="
                + url + ", timestamp=" + timestamp + ", user=" + user + ", upVote=" + upVote
                + ", downVote=" + downVote + "]";
    }
     
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + downVote;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + upVote;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        Discovery other = (Discovery) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (downVote != other.downVote)
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (upVote != other.upVote)
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }
}
