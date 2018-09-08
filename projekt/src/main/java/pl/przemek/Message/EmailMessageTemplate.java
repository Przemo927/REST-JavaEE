package pl.przemek.Message;

public class EmailMessageTemplate {
    private static String message="Click link below to continue registration"+
            "<br><br><a href=[baseUrl]/register/[user]>Continue</a>";

    public static String getPreparedMessage(String baseUrl,String username){
        String temporaryMessage=message.replace("[baseUrl]",baseUrl);
        return temporaryMessage.replace("[user]",username);
    }
}
