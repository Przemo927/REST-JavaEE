package pl.przemek.model;

public enum RoleType {

    ADMIN("admin"),USER("user");

    private RoleType(String roleType){
        this.roleType=roleType;
    }

    private String roleType;

    public String getRoleType() {
        return roleType;
    }
}
