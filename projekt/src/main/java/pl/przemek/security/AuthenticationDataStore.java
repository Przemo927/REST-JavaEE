package pl.przemek.security;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class AuthenticationDataStore implements Serializable {
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	private String encryptedPassword;
	

}
