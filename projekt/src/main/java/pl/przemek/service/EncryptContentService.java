package pl.przemek.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.przemek.model.User;
import pl.przemek.security.SignContent;
import pl.przemek.security.Utils.KeyUtils;
import pl.przemek.security.Utils.SignatureUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EncryptContentService {

    private SecurityKeyService keyService;
    private Logger logger;
    private final static String NEW_LINE = System.getProperty("line.separator");

    @Inject
    public EncryptContentService(SecurityKeyService keyService,DiscoveryService discoveryService,Logger logger){
        this.keyService=keyService;
        this.logger=logger;
    }

    public Optional<SignContent> endcryptContent(HttpServletRequest request, Object objectToEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, JsonProcessingException {
        SignContent signContent;
        User user=(User)request.getSession().getAttribute("user");
        List<String> listPrivateKeyString=keyService.getPrivateKeyAsStringByUsername(user.getUsername());
        if(listPrivateKeyString.isEmpty()) {
            logger.log(Level.WARNING,"[EncryptContentService] endcryptContent() PrivateKey wasn't found");
            return Optional.empty();
        }
        else {
            String privateKeyString=deleteSignsOfNewLine(listPrivateKeyString.get(0));
            PrivateKey privateKey=KeyUtils.generatePrivateKeyObjectFromString(privateKeyString);
            String objectAsString=convertObjectToJson(objectToEncrypt);
            byte[] sign=SignatureUtils.generateSignature("SHA256withRSA",privateKey, objectAsString);
            String signatureAsString=SignatureUtils.convertSignatureToString(sign);
            signContent=new SignContent();
            signContent.setSignedContent(objectToEncrypt);
            signContent.setSignature(signatureAsString);

        }
        return Optional.of(signContent);
    }


    private String deleteSignsOfNewLine(String content){
        if(content.contains(NEW_LINE))
            content=content.replaceAll("(\n)","");
        return content;
    }
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
