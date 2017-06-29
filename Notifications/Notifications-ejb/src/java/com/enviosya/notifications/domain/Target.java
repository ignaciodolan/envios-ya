package com.enviosya.notifications.domain;

import com.enviosya.logger.LoggerEnviosYa;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(
        mappedName = "jms/QueueNotifications", 
        activationConfig = {
            @ActivationConfigProperty(
                    propertyName = "destinationType", 
                    propertyValue = "javax.jms.Queue"
            )
        }
)
public class Target implements MessageListener {
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(Target.class);
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage messageReceived = (TextMessage) message;
            int typePosition = messageReceived.getText().indexOf("<type>");
            int messagePosition = messageReceived.getText().indexOf("<message>");
            String type = messageReceived.getText().substring(typePosition, messagePosition);
            String notificationMessage = messageReceived.getText().substring(messagePosition, messageReceived.getText().length());
            if(type.equals("email")){
                EmailFactory emailFactory = new EmailFactory();
                emailFactory.send(notificationMessage);
            }
            // TODO: Control if no type is provided and other notifiaction ways 
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }
    
}
