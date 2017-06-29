package com.enviosya.notifications.beans;

import com.enviosya.notifications.dto.MessageDTO;
import com.enviosya.notifications.exceptions.NotificationException;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import com.enviosya.logger.LoggerEnviosYa;
import javax.jms.Session;


@Stateless
@LocalBean
public class NotificationBean {
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/QueueNotifications")
    private Queue queue;
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(NotificationBean.class);

    public void sendMessage(MessageDTO messageDTO) throws NotificationException{
        try {
            if(messageDTO == null){
                throw new NotificationException("Message is null");
            }
            Connection connection;
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            TextMessage messageToSend = session.createTextMessage(messageDTO.getMessageToTransfer());
            MessageProducer producer = session.createProducer(queue);
            producer.send(messageToSend);
            session.close();
            connection.close();
        } catch (JMSException ex) {
            StringBuilder message = new StringBuilder();
            message.append("[JMS Exception]");
            message.append(ex.getMessage());
            logger.error(message.toString());
            throw new NotificationException(message.toString());
        }
    }


    
}
