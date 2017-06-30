
package com.enviosya.reviewchecksemantic.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.reviewchecksemantic.dto.ClientDTO;
import com.enviosya.reviewchecksemantic.dto.ReviewDTO;
import com.enviosya.reviewchecksemantic.exceptions.RequestException;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/QueueCheckComment")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ReviewCheckSemanticBean implements MessageListener {
    
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/QueueAddReview")
    private Queue queueAddReview;
    
    private final Gson gson = new Gson();
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ReviewCheckSemanticBean.class);
    
    public ReviewCheckSemanticBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            ReviewDTO reviewDTO = (ReviewDTO) ((ObjectMessage) message).getObject();
            checkSemantics(reviewDTO);
        } catch (JMSException | IOException | RequestException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    private void checkSemantics(ReviewDTO reviewDTO) throws IOException, ProtocolException, MalformedURLException, RequestException {
        // Mocking feeling always 1 for now
        reviewDTO.setFeeling(1);
        queueReview(reviewDTO);
    }
    
    private void queueReview(ReviewDTO review) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            ObjectMessage msg = session.createObjectMessage(review);
            MessageProducer producer = session.createProducer(queueAddReview);
            producer.send(msg);
            session.close();
            connection.close();
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }
       
}
