package pl.przemek.repository.inMemoryRepository;


import pl.przemek.model.Role;
import pl.przemek.model.User;
import pl.przemek.repository.JpaRoleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class JpaRoleRepositoryInMemoryImpl implements JpaRoleRepository {

    private List<Role> listOfRoles;

    public JpaRoleRepositoryInMemoryImpl(){
        listOfRoles=new ArrayList<>();
        populateRoleList();
    }
    @Override
    public void update(Role role, User user) {
        role.getUsers().add(user);
        for(int i=0;i<listOfRoles.size();i++){
            if(listOfRoles.get(i).getroleName().equals(role.getroleName())){
                listOfRoles.set(i,role);
            }
        }
    }

    @Override
    public List<Role> getRoles(String nameRole) {
        List<Role> temporaryList=new ArrayList<>();
        listOfRoles.forEach(role -> {
            if(nameRole!=null && nameRole.equals(role.getroleName()))
                temporaryList.add(role);
        });
        return temporaryList;
    }
    private void populateRoleList(){
        Role role;
        for(int i=0;i<10;i++){
            role=new Role();
            role.setDescription("description"+i);
            role.setroleName("rolename"+i);
            listOfRoles.add(role);
            role.setUsers(new HashSet<>());
        }
        role=new Role();
        role.setroleName("user");
        role.setDescription("description");
        role.setUsers(new HashSet<>());
        listOfRoles.add(role);
    }
    public List<Role> getListOfRoles() {
        return listOfRoles;
    }

    public void setListOfRoles(List<Role> listOfRoles) {
        this.listOfRoles = listOfRoles;
    }

    public void deleteRoleByRoleName(String roleName){
        Role role=null;
        for(int i=0;i<listOfRoles.size();i++){
            role=listOfRoles.get(i);
            if(roleName!=null && roleName.equals(role.getroleName()))
                listOfRoles.remove(role);
        }
    }
}
