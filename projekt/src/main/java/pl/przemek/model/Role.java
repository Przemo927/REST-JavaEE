package pl.przemek.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity	
@NamedQuery(name = "Role.findByName", query = "SELECT p FROM Role p WHERE p.roleName=:role_name")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
@Id
@Column(nullable = false, unique = true, name="role_name")
private String roleName;
@Column(length=250)
private String description;
@ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
@JoinTable(name = "user_role", 
   joinColumns = {@JoinColumn(name="role_name", referencedColumnName="role_name")},
   inverseJoinColumns = {@JoinColumn(name="username", referencedColumnName="username")})
private Set<User> users;

public Role(){}

public Role(String roleName,String description){
	this.roleName=roleName;
	this.description=description;	
}
@Override
public String toString() {
	return "Role [roleName=" + roleName + ", description=" + description + ", users=" + users + "]";
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
	if (roleName == null) {
		if (other.roleName != null)
			return false;
	} else if (!roleName.equals(other.roleName))
		return false;
	if (users == null) {
		if (other.users != null)
			return false;
	} else if (!users.equals(other.users))
		return false;
	return true;
}
public String getRoleName() {
	return roleName;
}
public void setRoleName(String roleName) {
	this.roleName = roleName;
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

