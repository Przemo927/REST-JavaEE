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
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "jms/queue/DLQ"),
})
public class DLQListener implements MessageListener {

    @Inject
    Logger logger;
    @Override
    public void onMessage(Message message) {
        Session mailSession= EmailUtils.createEmailSession();
        try {
            javax.mail.Message mailMessage=EmailUtils.createFailureMailMessage(mailSession,message.getBody(MessageWrapper.class));
            Transport transport=EmailUtils.getTransportProtocol(mailSession,EmailInformation.getTransportProtocol(),EmailInformation.getHostTransportProtocolEmail());
            transport.sendMessage(mailMessage,mailMessage.getAllRecipients());
        } catch (MessagingException | JMSException e) {
            logger.log(Level.SEVERE,"[DLQListener] onMessage()",e);
        }
    }
}
