package pl.przemek.service;

import pl.przemek.model.SecurityKey;
import pl.przemek.repository.JpaSecurityKeyRespository;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityKeyService {

    private JpaSecurityKeyRespository privateKeyRepo;
    private Logger logger;

    @Inject
    public SecurityKeyService(Logger logger,JpaSecurityKeyRespository privateKeyRepo){
        this.logger=logger;
        this.privateKeyRepo=privateKeyRepo;
    }

    List<String> getPrivateKeyAsStringByUsername(String userName){
        return privateKeyRepo.getPrivateKeyByUserName(userName);
    }

    public void addPrivateKey(SecurityKey securityKey){
        if(securityKey==null){
            logger.log(Level.SEVERE,"[SecurityKeyService] getPrivateKeyAsStringByUsername() serucityKey is null");
        }else {
            privateKeyRepo.add(securityKey);
        }
    }
}
