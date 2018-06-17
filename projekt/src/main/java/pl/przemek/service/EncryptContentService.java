package pl.przemek.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.przemek.model.User;
import pl.przemek.security.SignContent;
import pl.przemek.security.Utils.KeyUtils;
import pl.przemek.security.Utils.SignatureUtils;
import pl.przemek.service.DiscoveryService;
import pl.przemek.service.SecurityKeyService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class EncryptContentService {

    private DiscoveryService discoveryService;
    private SecurityKeyService keyService;

    @Inject
    public EncryptContentService( SecurityKeyService keyService){
        this.discoveryService=discoveryService;
        this.keyService=keyService;
    }
    public EncryptContentService(){

    }
    public SignContent endcryptContent(HttpServletRequest request,Object objectToEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, JsonProcessingException {
        User user=(User)request.getSession().getAttribute("user");
        String privateKeyString=keyService.getPrivateKeyAsStringByUsername(user.getUsername());
        if(privateKeyString==null)
            return null;
        else {
            privateKeyString=deleteSignsOfNewLine(privateKeyString);
            PrivateKey privateKey=KeyUtils.generatePrivateKeyObjectFromString(privateKeyString);
            String objectAsString=convertObjectToJson(objectToEncrypt);
            byte[] sign=SignatureUtils.generateSignature("SHA256withRSA",privateKey, objectAsString);
            String signatureAsString=SignatureUtils.convertSignatureToString(sign);
            SignContent signContent=new SignContent();
            signContent.setSignedContent(objectToEncrypt);
            signContent.setSignature(signatureAsString);
            return signContent;
        }
    }


    private String deleteSignsOfNewLine(String content){
        if(content.contains("\n"))
            content=content.replaceAll("(\n)","");
        return content;
    }
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
