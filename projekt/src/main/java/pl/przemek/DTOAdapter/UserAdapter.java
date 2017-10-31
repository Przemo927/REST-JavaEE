package pl.przemek.DTOAdapter;


import pl.przemek.model.User;

public class UserAdapter {
    private User user;

    public UserAdapter(User user) {
        this.user = user;
    }

    public User createNewUser(){
        User newUser=new User();
        newUser.setActive(this.user.isActive());
        newUser.setEmail(this.user.getEmail());
        newUser.setUsername(this.user.getUsername());
        return newUser;
    }
}
