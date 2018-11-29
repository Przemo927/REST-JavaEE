package pl.przemek.Message;

public class FailureEmailMessageTemplate {

    private final static String FAILURE_MESSAGE_TEMPLATE="Something went wrong we're trying to fix the problem";

    public static String getPreparedMessage(){
        return FAILURE_MESSAGE_TEMPLATE;
    }
}
