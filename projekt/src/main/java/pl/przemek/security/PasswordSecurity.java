package pl.przemek.security;

import de.rtner.security.auth.spi.PBKDF2Parameters;
import de.rtner.security.auth.spi.SimplePBKDF2;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordSecurity {

    private static Logger logger=Logger.getLogger(PasswordSecurity.class.getName());

    public static String hashPassword(String password) {
        if (password == null) return null;
        SimplePBKDF2 crypto = new SimplePBKDF2();
        fillParameters(crypto,password);
        return crypto.getFormatter().toString(crypto.getParameters());
    }
    protected static void fillParameters(SimplePBKDF2 crypto,String password) {
        PBKDF2Parameters params=crypto.getParameters();
        params.setHashCharset("UTF-8");
        params.setHashAlgorithm("HmacSHA1");
        params.setIterationCount(1000);
        params.setSalt(generateSalt(16));
        params.setDerivedKey(crypto.deriveKey(password,64));

    }
    protected static byte[] generateSalt(int saltSize) {
        byte[] salt = new byte[saltSize];
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE,"PasswordSecurity generateSalt()",e);
        }
        return salt;
    }
}
