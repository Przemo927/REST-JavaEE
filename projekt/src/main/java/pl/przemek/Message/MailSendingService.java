package pl.przemek.Message;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "jms/queue/Emails"),
})
public class MailSendingService implements MessageListener {

    @Inject
    Logger logger;

    @Override
    public void onMessage(Message message) {

            Session mailSession= EmailUtils.createEmailSession();
        try{
            MessageWrapper msg = message.getBody(MessageWrapper.class);
            javax.mail.Message mailMessage=EmailUtils.createMailMessage(mailSession,msg);
            Transport transport=EmailUtils.getTransportProtocol(mailSession,EmailInformation.getTransportProtocol(),EmailInformation.getHostTransportProtocolEmail());
            transport.sendMessage(mailMessage,mailMessage.getAllRecipients());
        } catch (MessagingException | JMSException e) {
            logger.log(Level.SEVERE,"[MailSendingService] onMessage()",e);
        }
    }


}
