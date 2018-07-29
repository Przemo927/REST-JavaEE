package pl.przemek.security.Utils;

import java.security.*;
import java.util.Base64;

public class SignatureUtils {

    public static byte[] generateSignature(String algorithm, PrivateKey privateKey,String contentToEncrypt) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(contentToEncrypt.getBytes());
        return signature.sign();
    }

    public static String convertSignatureToString(byte[] signature){
        return Base64.getEncoder().encodeToString(signature);
    }
}
