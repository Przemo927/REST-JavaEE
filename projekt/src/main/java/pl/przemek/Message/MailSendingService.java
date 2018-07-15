package pl.przemek.Message;

import pl.przemek.Utils.PropertiesFileUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
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
    private final String informationSenderProperties= "/loginInformationGmail.properties";
    private Properties propsEmail;
    private Properties propsInformationSender;
    @Override
    public void onMessage(Message message) {
            propsEmail= PropertiesFileUtils.getProperties(emailProperties);
            propsInformationSender=PropertiesFileUtils.getProperties(informationSenderProperties);
            fromEmail=propsInformationSender.getProperty("fromEmail");
            username=propsInformationSender.getProperty("username");
            password=propsInformationSender.getProperty("password");

            Session mailSession= createSession();
        try{
            MessageWrapper msg = message.getBody(MessageWrapper.class);
            javax.mail.Message mailMessage=createMailMessage(mailSession,msg);
            getTransportSMTPAndSendEmail(mailSession,mailMessage,username,password);
        } catch (MessagingException | JMSException | IOException e) {
                e.printStackTrace();
        }
    }
    private Session createSession(){
        Session mailSession= Session.getInstance(propsEmail,null);
        mailSession.setDebug(true);
        return mailSession;
    }
    private javax.mail.Message createMailMessage(Session mailSession, MessageWrapper msg) throws MessagingException, IOException {
        javax.mail.Message mailMessage=new MimeMessage(mailSession);
        mailMessage.setFrom(new InternetAddress(fromEmail));
        mailMessage.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(msg.getUser().getEmail()));
        mailMessage.setSubject("Email");
        mailMessage.setContent(createBodyOfEmail(msg.getMessage(),msg.getPublicKey()));
        return mailMessage;
    }
    private void getTransportSMTPAndSendEmail(Session mailSession,javax.mail.Message mailMessage,String username,String password) throws MessagingException {
        Transport transport=mailSession.getTransport("smtp");
        transport.connect("smtp.gmail.com",username,password);
        transport.sendMessage(mailMessage,mailMessage.getAllRecipients());

    }
    private Multipart createBodyOfEmail(String message, String publicKey) throws MessagingException, IOException {
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart.setContent(message,"text/html");
        File file=createFile(publicKey);
        DataSource source = new FileDataSource(file);
        messageBodyPart1.setDataHandler(new DataHandler(source));
        messageBodyPart1.setFileName("File");
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(messageBodyPart1);
        return multipart;
    }
    private File createFile(String publicKey){
        File file= null;
        FileOutputStream outputStream= null;
        try {
            file = File.createTempFile("PublicKey",".txt");
            outputStream = new FileOutputStream(file);
            outputStream.write(publicKey.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
