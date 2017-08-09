package pl.przemek.Message;


import pl.przemek.model.User;

import java.io.Serializable;

public class MessageWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private User user;
    public MessageWrapper(String message,User user) {
        this.message = message;
        this.user=user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
