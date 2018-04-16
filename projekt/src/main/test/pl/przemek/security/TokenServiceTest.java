package pl.przemek.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.util.Date;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Jwts.class})
public class TokenServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    private TokenService tokenService=spy(new TokenService());

    @Mock
    private Logger logger;
    @Mock
    private UriInfo uriInfo;

    @Test
    public void shouldGenerateNewToken(){
        PowerMockito.mockStatic(Jwts.class);
        String encryptedPassword="a4cb499fa31a6a228e16b7e4741d4fa3";
        String username="username";
        byte[] bytes="a4cb499fa31a6a228e16b7e4741d4fa3".getBytes();
        Key key=new SecretKeySpec(bytes,0,bytes.length,"DES");
        JwtBuilder jwtBuilder=mock(JwtBuilder.class);

        doReturn(key).when(tokenService).generateKey(anyString());
        when(Jwts.builder()).thenReturn(jwtBuilder);
        when(jwtBuilder.setSubject(anyString())).thenReturn(jwtBuilder);
        when(jwtBuilder.setIssuedAt(isA(Date.class))).thenReturn(jwtBuilder);
        when(jwtBuilder.setExpiration(isA(Date.class))).thenReturn(jwtBuilder);
        when(jwtBuilder.signWith(isA(SignatureAlgorithm.class),isA(Key.class))).thenReturn(jwtBuilder);

        tokenService.generateToken(username,encryptedPassword);

        verify(jwtBuilder,times(1)).setSubject(username);
        verify(jwtBuilder,times(1)).setExpiration(isA(Date.class));
        verify(jwtBuilder,times(1)).signWith(SignatureAlgorithm.HS512,key);

    }

    @Test
    public void shouldGenerateNewKey(){
        String key="key";
        Key secretKey=new SecretKeySpec(key.getBytes(),0,key.getBytes().length,"DES");
        Key secretKey1=new SecretKeySpec("KEY".getBytes(),0,"KEY".getBytes().length,"DES");
        assertEquals(secretKey,tokenService.generateKey(key));
        assertTrue(!secretKey1.equals(tokenService.generateKey(key)));
    }


}
