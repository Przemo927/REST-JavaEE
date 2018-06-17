package pl.przemek.security;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.PrivateKey;

@SessionScoped
public class KeyDataStore implements Serializable {

    private PrivateKey privateKey;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
