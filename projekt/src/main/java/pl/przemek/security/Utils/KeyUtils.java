package pl.przemek.security.Utils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    public static PrivateKey generatePrivateKeyObjectFromString(String privateKeyAsString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyDecoded = Base64.getDecoder().decode(privateKeyAsString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyDecoded);
        return keyFactory.generatePrivate(keySpec);
    }
    public static KeyPair generatePairOfKeys() throws NoSuchProviderException, NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048,secureRandom);
        return keyPairGenerator.generateKeyPair();
    }
    public static String convertKeyToString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
