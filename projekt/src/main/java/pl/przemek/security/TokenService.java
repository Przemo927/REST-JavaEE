package pl.przemek.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenService {
    private String token;
    @Inject
    Logger logger;
    @Context
    UriInfo uriInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String generateToken(String username,String encryptedPassword){
        Key key = generateKey(encryptedPassword);
        token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(10)))
                .signWith(SignatureAlgorithm.HS512, key).compact();


        return token;
    }
    
    public Key generateKey(String key){
        return new SecretKeySpec(key.getBytes(), 0, key.getBytes().length, "DES");
    }

    Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
