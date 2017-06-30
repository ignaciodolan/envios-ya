package com.enviosya.review.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.review.dto.ReviewDTO;
import com.enviosya.review.exceptions.RequestException;
import com.enviosya.review.exceptions.ReviewException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

@Stateless
@LocalBean
public class ReviewBean{
    
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/QueueValidateClient")
    private Queue queueValidate;
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ReviewBean.class);
    
    private final String URL_GET_REVIEW_READ = "http://localhost:8080/ReviewRead-war/review/{id}";
    
    private final String URL_GET_REVIEWS = "http://localhost:8080/ReviewRead-war/review";
    
    private static final String NULL_ENTITY = " Entity to build is null";
    
    private static final String INVALID_RATING = "Invalid rating sent";
    
    private static final int STATUS_PENDING = 0;
    
    private final Gson gson = new Gson();
    
    
    public ReviewDTO create(ReviewDTO reviewDTO) throws ReviewException{
        try {
            if (reviewDTO == null) {
                throw new ReviewException(NULL_ENTITY);
            }
            if (nullValuesInReviewExist(reviewDTO)) {
                throw new ReviewException(NULL_ENTITY);
            }
            if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
                throw new ReviewException(INVALID_RATING);
            }
            
            reviewDTO.setStatus(STATUS_PENDING);
            
            sendReviewToClientValidation(reviewDTO);
            
        } catch (JMSException e) {
            StringBuilder message = new StringBuilder();
            message.append("[JMSException]");
            message.append(e.getMessage());
            logger.error(message.toString());
            throw new ReviewException(e.getMessage());
        }
        
        return reviewDTO;
            
    }

    public List<ReviewDTO> getReviewList() throws ReviewException{
        String reviewJSON;
        try {
            reviewJSON = this.sendGet(URL_GET_REVIEWS);
        } catch (IOException | RequestException ex) {
            StringBuilder message = new StringBuilder();
            message.append("[IOException | RequestException]");
            message.append(ex.getMessage());
            logger.error(message.toString());
            throw new ReviewException(ex.getMessage());
        }
        
        Type listReviewDTOType = new TypeToken<List<ReviewDTO>>(){}.getType();
        List<ReviewDTO> reviewList = (List<ReviewDTO>) gson.fromJson(reviewJSON, listReviewDTOType);
        
        return reviewList;
    }

    public ReviewDTO getReviewById(Long id) throws ReviewException{
        String urlReviewRead = URL_GET_REVIEW_READ;
        urlReviewRead = urlReviewRead.replace("{id}",id.toString());
        String reviewJSON;
        try {
            reviewJSON = this.sendGet(urlReviewRead);
        } catch (IOException | RequestException ex) {
            StringBuilder message = new StringBuilder();
            message.append("[IOException | RequestException]");
            message.append(ex.getMessage());
            logger.error(message.toString());
            throw new ReviewException(ex.getMessage());
        }
        ReviewDTO reviewDTO = gson.fromJson(reviewJSON, ReviewDTO.class);
        
        return reviewDTO;
    }
    // HTTP GET request
    private String sendGet(String url) throws MalformedURLException, IOException, RequestException  {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            
            if(responseCode != 200){
                StringBuilder message = new StringBuilder();
                message.append("[URL]");
                message.append(url);
                message.append(" responded with response code: ");
                message.append(responseCode);
                throw new RequestException(message.toString());
            }
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            return response.toString();

    }
    private boolean nullValuesInReviewExist(ReviewDTO reviewDTO) {
        return reviewDTO.getClientId() == null || reviewDTO.getComment() == null || reviewDTO.getShipmentId() == null;
    }

    private void sendReviewToClientValidation(ReviewDTO reviewDTO) throws JMSException {
         try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            ObjectMessage msg = session.createObjectMessage(reviewDTO);
            MessageProducer producer = session.createProducer(queueValidate);         
            producer.send(msg);
            session.close();
            connection.close();
        } catch (JMSException ex) {
            throw ex;
        }
    }
}
