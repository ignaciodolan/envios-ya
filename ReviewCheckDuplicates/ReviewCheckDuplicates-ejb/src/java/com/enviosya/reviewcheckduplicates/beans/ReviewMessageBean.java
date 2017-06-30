
package com.enviosya.reviewcheckduplicates.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.reviewcheckduplicates.dto.ClientDTO;
import com.enviosya.reviewcheckduplicates.dto.ReviewDTO;
import com.enviosya.reviewcheckduplicates.exceptions.RequestException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.lang.reflect.Type;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(
            propertyName = "destinationLookup", 
            propertyValue = "jms/QueueCheckDuplicates"
    ),
    @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"
    )
})
public class ReviewMessageBean implements MessageListener {
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/QueueCheckComment")
    private Queue queueCheckComment;
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ReviewMessageBean.class);

    private static final String URL_GET_REVIEWS = "http://localhost:8080/ReviewRead-war/review/exists/{shipmentId}";
    private static final String URL_GET_CLIENT = "http://localhost:8080/Clients-war/client/{id}";
    
    public ReviewMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            ReviewDTO reviewDTO = (ReviewDTO) ((ObjectMessage) message).getObject();
            checkNoDuplicate(reviewDTO);
        } catch (JMSException | IOException | RequestException ex) {
            logger.error(ex.getMessage());
        }
    }

    private void checkNoDuplicate(ReviewDTO reviewDTO) throws IOException, ProtocolException, MalformedURLException, RequestException {
        if (findDuplicatedReview(reviewDTO.getShipmentId(), reviewDTO.getClientId()).isEmpty()) {
            queueReview(reviewDTO);
        } else {
            String mensaje = createDuplicatedReviewMessage(reviewDTO).toString();
            if(!mensaje.isEmpty()){
               queueMessage(mensaje); 
            }            
        }
    }
    
    public StringBuilder createDuplicatedReviewMessage(ReviewDTO reviewDTO) throws IOException, ProtocolException, MalformedURLException, RequestException {
        StringBuilder message = new StringBuilder();
        ClientDTO cliente = findClientByRemoteId(reviewDTO.getClientId());
        if (cliente != null) {
            message.append("<start-email>]");
            message.append(cliente.getEmail());
            message.append("<end-email>");
            message.append("<start-subject>");
            message.append("Review duplicated");
            message.append("<end-subject>");
            message.append(";*");
            message.append("<start-message>");
            message.append("There is already a review for that shipment");
            message.append("<end-message>");
        }
        return message;
    }
    
    private List<ReviewDTO> findDuplicatedReview(Long shipmentId, Long clientId) throws ProtocolException, IOException, MalformedURLException, RequestException {
        String urlGetReviews = URL_GET_REVIEWS;
        urlGetReviews = urlGetReviews.replace("{shipmentId}", shipmentId.toString());
        urlGetReviews = urlGetReviews.replace("{clientId}", clientId.toString());
        String reviewsJson = sendGet(urlGetReviews);
        Type listReviewDTOType = new TypeToken<List<ReviewDTO>>(){}.getType();
        List<ReviewDTO> reviews = (List<ReviewDTO>) gson.fromJson(reviewsJson, listReviewDTOType);
        return reviews;
    }
    
    private ClientDTO findClientByRemoteId(Long clientId) throws ProtocolException, IOException, MalformedURLException, RequestException {
        ClientDTO clientReturn;
        String urlGetClient = URL_GET_CLIENT;
        urlGetClient = urlGetClient.replace("{id}", clientId.toString());
        String clientJson = sendGet(urlGetClient);
        clientReturn = gson.fromJson(clientJson, ClientDTO.class);
        return clientReturn;
    }
    
    private String sendGet(String url) throws MalformedURLException, ProtocolException, IOException, RequestException{
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
    
    
    
    private void queueMessage(String message) {
        URL url;
        try {
            url = new URL("http://localhost:8080/Notifications-war/notification");
            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) url.openConnection();
                try {
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(message.getBytes());
                    os.flush();
                    BufferedReader reader;
                    try {
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = reader != null ? reader.readLine() : null;
                        if (output != null) {

                        }
                    } catch (IOException ex) {
                        logger.error(ex.getMessage());
                    }
                    conn.disconnect();
                } catch (ProtocolException ex) {
                    logger.error(ex.getMessage());
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    private void queueReview(ReviewDTO review) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            ObjectMessage msg = session.createObjectMessage(review);
            MessageProducer producer = session.createProducer(queueCheckComment);
            producer.send(msg);
            session.close();
            connection.close();
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    
}
