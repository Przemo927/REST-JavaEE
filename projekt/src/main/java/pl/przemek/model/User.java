package pl.przemek.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

import pl.przemek.validation.UsernameMatching;

@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT p FROM User p"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT p FROM User p WHERE p.username=:username")
    })
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "user_id",nullable = false, unique = true)
    private long id;
	@Column(nullable = false, unique = true, length=45)
	@UsernameMatching
    private String username;
	@Column(nullable = false, unique = true, length=60)
    private String email;
	@Column(nullable = false, length=45)
    private String password;
	@Column(nullable = false)
    private boolean active;

     
    public User() { }
     
    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.email = user.email;
        this.password = user.password;
    }
     
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
     
    public boolean isActive() {
        return active;
    }
 
    public void setActive(boolean active) {
        this.active = active;
    }
 
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", password="
                + password + ", active=" + active + "]";
    }
 
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        User other = (User) obj;
        if (active != other.active)
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (id != other.id)
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }


}