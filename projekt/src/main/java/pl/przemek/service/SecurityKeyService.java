package pl.przemek.service;

import pl.przemek.model.SecurityKey;
import pl.przemek.repository.JpaSecurityKeyRespository;

import javax.inject.Inject;

public class SecurityKeyService {

    JpaSecurityKeyRespository privateKeyRepo;

    @Inject
    public SecurityKeyService(JpaSecurityKeyRespository privateKeyRepo){
        this.privateKeyRepo=privateKeyRepo;
    }

    public String getPrivateKeyAsStringByUsername(String userName){
        return privateKeyRepo.getPrivateKeyByUserName(userName);
    }

    public void addPrivateKey(SecurityKey securityKey){
        privateKeyRepo.add(securityKey);
    }
}
