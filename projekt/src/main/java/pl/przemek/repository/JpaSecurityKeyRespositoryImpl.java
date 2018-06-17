package pl.przemek.repository;

import pl.przemek.model.SecurityKey;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaSecurityKeyRespositoryImpl extends JpaRepository<SecurityKey> implements JpaSecurityKeyRespository {

    @RolesAllowed({"admin","user"})
    @Override
    public String getPrivateKeyByUserName(String userName) {
        TypedQuery<String> findByUsernameQuery=em.createNamedQuery("SecurityKey.findByUserName",String.class);
        findByUsernameQuery.setParameter("username",userName);
        List<String> listOfKeys=findByUsernameQuery.getResultList();
        if(listOfKeys.isEmpty())
            return null;
        return listOfKeys.get(0);
    }
    @PermitAll
    @Override
    public void add(SecurityKey securityKey){
        em.persist(securityKey);

    }
}
