package pl.przemek.Message;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

public class MailService {
    @Inject
    private JMSContext jmsContext;

    @Resource(mappedName = "jms/queue/Emails")
    private Queue mailQueue;

    public void sendMessage(MessageWrapper message) {
        JMSProducer producer = jmsContext.createProducer();
        producer.send(mailQueue, message);
    }
}
