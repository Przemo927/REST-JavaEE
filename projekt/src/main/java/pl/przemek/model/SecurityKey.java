package pl.przemek.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name="SecurityKey.findByUserName",query="SELECT p.privateKey FROM SecurityKey p WHERE p.username=:username")
})
@Entity
public class SecurityKey implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id;

    @Size(max=45)
    @NotNull
    @Column(nullable = false, unique = true, length=45)
    private String username;

    @NotNull
    @Column(nullable = false, unique = true, length = 2000)
    private String privateKey;

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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityKey)) return false;

        SecurityKey that = (SecurityKey) o;

        if (id != that.id) return false;
        if (!username.equals(that.username)) return false;
        return privateKey.equals(that.privateKey);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + username.hashCode();
        result = 31 * result + privateKey.hashCode();
        return result;
    }
}
