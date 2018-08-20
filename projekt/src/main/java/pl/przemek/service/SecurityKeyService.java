package pl.przemek.service;

import pl.przemek.model.SecurityKey;
import pl.przemek.repository.JpaSecurityKeyRespository;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityKeyService {

    JpaSecurityKeyRespository privateKeyRepo;
    private Logger logger;

    @Inject
    public SecurityKeyService(Logger logger,JpaSecurityKeyRespository privateKeyRepo){
        this.logger=logger;
        this.privateKeyRepo=privateKeyRepo;
    }

    public List<String> getPrivateKeyAsStringByUsername(String userName){
        List<String> privateKeyByUserName=null;
        try {
            privateKeyByUserName=privateKeyRepo.getPrivateKeyByUserName(userName);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[SecurityKeyService] getPrivateKeyAsStringByUsername() username="+userName,e);
            return Collections.emptyList();
        }
        return privateKeyByUserName;
    }

    public void addPrivateKey(SecurityKey securityKey){
        try {
            privateKeyRepo.add(securityKey);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[SecurityKeyService] getPrivateKeyAsStringByUsername() serucityKey="+securityKey,e);
        }
    }
}
