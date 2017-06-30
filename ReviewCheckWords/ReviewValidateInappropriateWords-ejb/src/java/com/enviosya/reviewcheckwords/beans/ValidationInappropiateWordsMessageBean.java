

package com.enviosya.reviewcheckwords.beans;

import com.enviosya.reviewcheckwords.dto.ReviewDTO;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import com.enviosya.logger.LoggerEnviosYa;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.jms.MessageProducer;
import javax.jms.Queue;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(
            propertyName = "destinationLookup",
            propertyValue = "jms/QueueCheckWords"
    ),
    @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"
    )
})
public class ValidationInappropiateWordsMessageBean implements MessageListener {
    
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/QueueSemanticReview")
    private Queue queueSemanticReview;
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ValidationInappropiateWordsMessageBean.class);
    private static final String blacklist_path = "c:\\blacklist\\blacklist.txt";
    
    public ValidationInappropiateWordsMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            ReviewDTO reviewDTO = (ReviewDTO) ((ObjectMessage) message).getObject();
            if (existsInappropriatedWords(reviewDTO.getComment())) {
                // Changes to REJECTED
                reviewDTO.setStatus(2);
            }
            queueReview(reviewDTO);
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    
    private void queueReview(ReviewDTO reviewDTO) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            ObjectMessage message = session.createObjectMessage(reviewDTO);
            MessageProducer producer = session.createProducer(queueSemanticReview);
            producer.send(message);
            session.close();
            connection.close();
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }

    private boolean existsInappropriatedWords(String comment) {
        try {
            String badWords;
            FileReader file = new FileReader(blacklist_path);
            BufferedReader reader = new BufferedReader(file);
            while ((badWords = reader.readLine()) != null) {
                if (comment.contains(badWords)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
            return false;
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }
}
