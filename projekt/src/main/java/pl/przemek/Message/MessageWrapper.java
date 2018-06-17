package pl.przemek.Message;


import pl.przemek.model.User;

import java.io.Serializable;

public class MessageWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private User user;

    private String publicKey;
    public MessageWrapper(String message,User user, String publicKey) {
        this.message = message;
        this.user=user;
        this.publicKey=publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
