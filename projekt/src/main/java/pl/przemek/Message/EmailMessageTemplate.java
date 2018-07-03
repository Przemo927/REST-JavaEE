package pl.przemek.Message;

public class EmailMessageTemplate {
    private static String message="Click link below to continue registration"+
            "<br><br><a href=http://localhost:8080/projekt" +
            "/api/register/[user]>Continue</a>";

    public static String getPreparedMessage(String username){
       return message.replace("[user]",username);
    }
}
