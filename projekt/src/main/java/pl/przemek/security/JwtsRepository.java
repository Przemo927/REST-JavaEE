package pl.przemek.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.Key;

public class JwtsRepository {

    public static Jws<Claims> checkToken(Key key, String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }
}
