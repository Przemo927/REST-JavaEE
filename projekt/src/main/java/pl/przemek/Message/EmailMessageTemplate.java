package pl.przemek.Message;

public class EmailMessageTemplate {
    private final static String MESSAGE_TEMPLATE="Click link below to continue registration"+
            "<br><br><a href=[baseUrl]register/[user]>Continue</a>";
    private final static String  URL_SEPARATOR="/";

    public static String getPreparedMessage(String baseUrl,String username){
        String temporaryMessage;
        if(baseUrl.endsWith(URL_SEPARATOR)) {
            temporaryMessage=MESSAGE_TEMPLATE.replace("[baseUrl]",baseUrl);
        }else {
            temporaryMessage=MESSAGE_TEMPLATE.replace("[baseUrl]",baseUrl+URL_SEPARATOR);
        }

        return temporaryMessage.replace("[user]",username);
    }
}
