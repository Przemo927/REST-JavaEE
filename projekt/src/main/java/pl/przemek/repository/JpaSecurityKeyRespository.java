package pl.przemek.repository;


import pl.przemek.model.SecurityKey;

import java.util.List;

public interface JpaSecurityKeyRespository {
    public void add(SecurityKey key);
    public void remove(SecurityKey key);
    public SecurityKey update(SecurityKey key);
    public List<String> getPrivateKeyByUserName(String userName);
}
