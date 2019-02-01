package pl.przemek.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.URL;
import pl.przemek.validation.ForbiddenWord;
import pl.przemek.validation.URLUnique;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
    @NamedQuery(name = "Discovery.findAll", query = "SELECT p FROM Discovery p"),
    @NamedQuery(name = "Discovery.search", query = "SELECT p FROM Discovery p WHERE p.name=:name"),
    @NamedQuery(name = "Discovery.getDiscoveryWithComments", query = "SELECT p FROM Discovery p LEFT JOIN FETCH p.comments WHERE p.id=:discovery_id")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "Discovery.findAllWithLimit",query = "SELECT * FROM discovery limit :begin,:quantity",resultClass = Discovery.class),
        @NamedNativeQuery(name="Discovery.findAllWithLimitOrderByDate",query = "SELECT * FROM discovery ORDER BY timestamp limit :begin,:quantity",resultClass = Discovery.class)
})
public class Discovery implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "discovery_id",nullable = false, unique = true)
	private long id;
	@Size(min=1,max=100)
    @NotNull
	@Column(nullable = false, length=100)
    private String name;
	@Size(min=1,max=250)
    @NotNull
    @ForbiddenWord
	@Column(nullable = false, length=250)
    private String description;
    @Size(min=1,max=200)
    @NotNull
    @URL
    @URLUnique
	@Column(nullable = false, length=200, unique = true)
    private String url;
	@Column(nullable = false)
    private Timestamp timestamp;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comments;

    @NotNull
    @Column(nullable = false)
    private int upVote;
    @NotNull
    @Column(nullable = false)
    private int downVote;

    public Discovery(){
    }

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
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Discovery [id=" + id + ", name=" + name + ", description=" + description + ", url="
                + url + ", timestamp=" + timestamp + ", user=" + user + ", upVote=" + upVote
                + ", downVote=" + downVote + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discovery)) return false;
        Discovery discovery = (Discovery) o;
        return id == discovery.id &&
                upVote == discovery.upVote &&
                downVote == discovery.downVote &&
                Objects.equals(name, discovery.name) &&
                Objects.equals(description, discovery.description) &&
                Objects.equals(url, discovery.url) &&
                Objects.equals(timestamp, discovery.timestamp) &&
                Objects.equals(user, discovery.user) &&
                Objects.equals(comments, discovery.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, url, timestamp, user, comments, upVote, downVote);
    }
}
