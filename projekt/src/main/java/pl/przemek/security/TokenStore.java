package pl.przemek.security;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class TokenStore implements Serializable {
    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
