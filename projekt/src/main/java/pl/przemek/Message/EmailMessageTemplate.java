package pl.przemek.Message;

import java.io.File;
import java.nio.file.Path;
import java.security.CodeSource;

public class EmailMessageTemplate {
    private static String message="Click link below to continue registration"+
            "<br><br><a href=[baseUrl]register/[user]>Continue</a>";
    private final static String  URL_SEPARATOR="/";

    public static String getPreparedMessage(String baseUrl,String username){
        String temporaryMessage;
        if(baseUrl.endsWith(URL_SEPARATOR)) {
            temporaryMessage=message.replace("[baseUrl]",baseUrl);
        }else {
            temporaryMessage=message.replace("[baseUrl]",baseUrl+URL_SEPARATOR);
        }

        return temporaryMessage.replace("[user]",username);
    }
}
