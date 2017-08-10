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

import static com.sun.org.apache.xalan.internal.utils.SecuritySupport.getResourceAsStream;

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
    @Override
    public void onMessage(Message message) {
        try {
            Properties propsEmail=new Properties();
            Properties propsGmail=new Properties();
            ClassLoader loaderEmailInformation = Thread.currentThread().getContextClassLoader();
            ClassLoader loaderLoginGmailInformation = Thread.currentThread().getContextClassLoader();
            try(
                    InputStream resourceStreamEmail = loaderEmailInformation.getResourceAsStream(emailProperties);
                    InputStream resourceStreamGmail = loaderEmailInformation.getResourceAsStream(gmailProperties);
            ){
                    propsEmail.load(resourceStreamEmail);
                    propsGmail.load(resourceStreamGmail);
                    fromEmail=propsGmail.getProperty("fromEmail");
                    username=propsGmail.getProperty("username");
                    password=propsGmail.getProperty("password");
            }

            MessageWrapper msg = message.getBody(MessageWrapper.class);

            Session mailSession= Session.getDefaultInstance(propsEmail,null);
            mailSession.setDebug(true);

            javax.mail.Message mailMessage=createMailMessage(mailSession,msg.getMessage(),msg.getUser());
            getTransportAndSendEmail(mailSession,mailMessage,username,password);
        } catch (MessagingException e) {
                e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private javax.mail.Message createMailMessage(Session mailSession, String message,User user) throws MessagingException {
        javax.mail.Message mailMessage=new MimeMessage(mailSession);
        mailMessage.setFrom(new InternetAddress(fromEmail));
        mailMessage.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(user.getEmail()));
        mailMessage.setContent(message,"text/html");
        mailMessage.setSubject("Email");
        return mailMessage;
    }
    private void getTransportAndSendEmail(Session mailSession,javax.mail.Message mailMessage,String username,String password) throws MessagingException {
        Transport transport=mailSession.getTransport("smtp");
        transport.connect("smtp.gmail.com",username,password);
        transport.sendMessage(mailMessage,mailMessage.getAllRecipients());

    }
}
