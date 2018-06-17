package pl.przemek.repository;


import pl.przemek.model.SecurityKey;

public interface JpaSecurityKeyRespository {
    public void add(SecurityKey key);
    public void remove(SecurityKey key);
    public SecurityKey update(SecurityKey key);
    public String getPrivateKeyByUserName(String userName);
}
