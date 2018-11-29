package pl.przemek.Message;

import pl.przemek.Utils.PropertiesFileUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Properties;

@Startup
@Singleton
public class EmailInformation {

    private static String senderEmail;
    private static String loginSenderEmail;
    private static String passwordSenderEmail;
    private static Properties EmailSendingInformation;
    private static String transportProtocol;
    private static String hostTransportProtocolEmail;
    private static final String EMAIL_PROPERTIES= "/InformationEmail.properties";
    private static final String INFORMATION_SENDER_PROPERTIES= "/EmailSender.properties";

    public static String getSenderEmail() {
        return senderEmail;
    }

    public static String getLoginSenderEmail() {
        return loginSenderEmail;
    }

    public static String getPasswordSenderEmail() {
        return passwordSenderEmail;
    }

    public static Properties getEmailSendingInformation() {
        return EmailSendingInformation;
    }

    public static String getTransportProtocol() {
        return transportProtocol;
    }

    public static String getHostTransportProtocolEmail() {
        return hostTransportProtocolEmail;
    }

    private EmailInformation(){
    }

    @PostConstruct
    public void gatherInformation(){
        EmailSendingInformation= PropertiesFileUtils.getProperties(EMAIL_PROPERTIES);
        Properties propsInformationSender= PropertiesFileUtils.getProperties(INFORMATION_SENDER_PROPERTIES);
        senderEmail=propsInformationSender.getProperty("fromEmail");
        loginSenderEmail=propsInformationSender.getProperty("username");
        passwordSenderEmail=propsInformationSender.getProperty("password");
        transportProtocol=propsInformationSender.getProperty("transportProtocol");
        hostTransportProtocolEmail=propsInformationSender.getProperty("hostTransportProtocolEmail");
    }
}
