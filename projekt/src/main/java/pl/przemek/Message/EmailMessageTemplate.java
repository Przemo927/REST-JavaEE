package pl.przemek.Message;

import pl.przemek.rest.utils.ResponseUtils;

public class EmailMessageTemplate {
    private final static String MESSAGE_TEMPLATE="Click link below to continue registration"+
            "<br><br><a href=[baseUrl]register/[user]>Continue</a>";

    public static String getPreparedMessage(String baseUrl,String username){
        String temporaryMessage;
        if(baseUrl.endsWith(ResponseUtils.URL_SEPARATOR)) {
            temporaryMessage=MESSAGE_TEMPLATE.replace("[baseUrl]",baseUrl);
        }else {
            temporaryMessage=MESSAGE_TEMPLATE.replace("[baseUrl]",baseUrl+ ResponseUtils.URL_SEPARATOR);
        }

        return temporaryMessage.replace("[user]",username);
    }
}
