package pl.przemek.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

@Entity	
@NamedQuery(name = "Role.findByName", query = "SELECT p FROM Role p WHERE p.role_name=:role_name")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
@Id
@Column(nullable = false, unique = true)
private String role_name;
@Column(length=250)
private String description;
@ManyToMany(cascade = CascadeType.PERSIST,fetch=FetchType.EAGER)
@JoinTable(name = "user_role", 
   joinColumns = {@JoinColumn(name="role_name", referencedColumnName="role_name")},
   inverseJoinColumns = {@JoinColumn(name="username", referencedColumnName="username")})
private Set<User> users;

public Role(){}

public Role(String role_name,String description){
	this.role_name=role_name;
	this.description=description;	
}
@Override
public String toString() {
	return "Role [role_name=" + role_name + ", description=" + description + ", users=" + users + "]";
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((role_name == null) ? 0 : role_name.hashCode());
	result = prime * result + ((users == null) ? 0 : users.hashCode());
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
	Role other = (Role) obj;
	if (description == null) {
		if (other.description != null)
			return false;
	} else if (!description.equals(other.description))
		return false;
	if (role_name == null) {
		if (other.role_name != null)
			return false;
	} else if (!role_name.equals(other.role_name))
		return false;
	if (users == null) {
		if (other.users != null)
			return false;
	} else if (!users.equals(other.users))
		return false;
	return true;
}
public String getRole_name() {
	return role_name;
}
public void setRole_name(String role_name) {
	this.role_name = role_name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}

public Set<User> getUsers() {
	return users;
}

public void setUsers(Set<User> users) {
	this.users = users;
}


}

