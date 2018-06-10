package pl.przemek.security;

import de.rtner.security.auth.spi.PBKDF2Parameters;
import de.rtner.security.auth.spi.SimplePBKDF2;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordSecurity {
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        if (password == null) return null;
        SimplePBKDF2 crypto = new SimplePBKDF2();
        crypto=fillParameters(crypto,password);
        return crypto.getFormatter().toString(crypto.getParameters());
    }
    protected SimplePBKDF2 fillParameters(SimplePBKDF2 crypto,String password) throws NoSuchAlgorithmException {
        PBKDF2Parameters params=crypto.getParameters();
        params.setHashCharset("UTF-8");
        params.setHashAlgorithm("HmacSHA1");
        params.setIterationCount(1000);
        params.setSalt(generateSalt(16));
        params.setDerivedKey(crypto.deriveKey(password,64));
        return crypto;

    }
    protected byte[] generateSalt(int saltSize) {
        byte[] salt = new byte[saltSize];
        SecureRandom secureRandom= null;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        secureRandom.nextBytes(salt);
        return salt;
    }
}
