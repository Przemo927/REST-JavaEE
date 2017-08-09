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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "jms/queue/Emails"),
})
public class MailSendingService implements MessageListener {
    String fromEmail;
    String username;
    String password;
    String fileName="/InformationGmail.properties";
    Properties prop=System.getProperties();
    Properties props;
    FileInputStream input;
    @Override
    public void onMessage(Message message) {
        try {
            props=new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceStream = loader.getResourceAsStream(fileName)){
                props.load(resourceStream);}
            MessageWrapper msg = message.getBody(MessageWrapper.class);



            Session mailSession= Session.getDefaultInstance(props,null);
            mailSession.setDebug(true);

            javax.mail.Message mailMessage=createMailMessage(mailSession,msg.getMessage(),msg.getUser());


            Transport transport=mailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com",username,password);
            transport.sendMessage(mailMessage,mailMessage.getAllRecipients());
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
}
