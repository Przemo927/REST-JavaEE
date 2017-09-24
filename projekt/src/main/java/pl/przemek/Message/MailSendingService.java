package pl.przemek.Message;

import pl.przemek.model.User;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "jms/queue/Emails"),
})
public class MailSendingService implements MessageListener {

    private String fromEmail;
    private String username;
    private String password;
    private final String emailProperties= "/InformationEmail.properties";
    private final String gmailProperties= "/loginInformationGmail.properties";
    private Properties propsEmail;
    private Properties propsGmail;
    @Override
    public void onMessage(Message message) {
            propsEmail=openPropertiesFile(emailProperties);
            propsGmail=openPropertiesFile(gmailProperties);
            fromEmail=propsGmail.getProperty("fromEmail");
            username=propsGmail.getProperty("username");
            password=propsGmail.getProperty("password");

            Session mailSession= createSession();
        try{
            MessageWrapper msg = message.getBody(MessageWrapper.class);
            javax.mail.Message mailMessage=createMailMessage(mailSession,msg.getMessage(),msg.getUser());
            getTransportSMTPAndSendEmail(mailSession,mailMessage,username,password);
        } catch (MessagingException | JMSException e) {
                e.printStackTrace();
        }
    }
    private Properties openPropertiesFile(String fileName){
        Properties property=new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream resourceStream = loader.getResourceAsStream(fileName)
        ){property.load(resourceStream);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }
    private Session createSession(){
        Session mailSession= Session.getInstance(propsEmail,null);
        mailSession.setDebug(true);
        return mailSession;
    }
    private javax.mail.Message createMailMessage(Session mailSession, String message,User user) throws MessagingException {
        javax.mail.Message mailMessage=new MimeMessage(mailSession);
        mailMessage.setFrom(new InternetAddress(fromEmail));
        mailMessage.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(user.getEmail()));
        mailMessage.setContent(message,"text/html");
        mailMessage.setSubject("Email");
        return mailMessage;
    }
    private void getTransportSMTPAndSendEmail(Session mailSession,javax.mail.Message mailMessage,String username,String password) throws MessagingException {
        Transport transport=mailSession.getTransport("smtp");
        transport.connect("smtp.gmail.com",username,password);
        transport.sendMessage(mailMessage,mailMessage.getAllRecipients());

    }
}
