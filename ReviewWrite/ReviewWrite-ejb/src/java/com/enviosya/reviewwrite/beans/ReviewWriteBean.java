/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.reviewwrite.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.reviewwrite.dao.ReviewWriteDAO;
import com.enviosya.reviewwrite.dto.ClientDTO;
import com.enviosya.reviewwrite.dto.ReviewDTO;
import com.enviosya.reviewwrite.entities.ReviewEntity;
import com.enviosya.reviewwrite.entities.ReviewStatus;
import com.google.gson.Gson;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(
            propertyName = "destinationLookup",
            propertyValue = "jms/QueueAddReview"
    ),
    @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"
    )
})
public class ReviewWriteBean implements MessageListener {
    
    private final Gson gson = new Gson();
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ReviewWriteBean.class);
    
    @Context 
    private HttpServletRequest request;

    @EJB
    private ReviewWriteDAO reviewWriteDAO;

    
    private static final String NULL_ENTITY = " Entity to build is null";
    
    private static final String REQUIRED_BLANK_FIELDS
            = " The operation could not be completed because there are required fields empty";
    
    private static final String QUALIFICATION_OUTSIDE_RANGE
            = "The operation could not be completed because the rating must be between 1 and 5";
    
    private static final String STATUS_OUTSIDE_RANGE
            = "The operation could not be completed because the status must be between 0 and 2";
    
    private static final String FEELING_OUTSIDE_RANGE
            = "The operation could not be completed because the feeling must be between 0 and 2";
    
    public ReviewWriteBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            ReviewDTO reviewDTO = (ReviewDTO) ((ObjectMessage) message).getObject();
            addReview(reviewDTO);
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }
    public void addReview (ReviewDTO reviewDTO) {
        String errorMessage = createMessageReviewError(reviewDTO).toString();
        if (reviewDTO == null) {
            logger.error(NULL_ENTITY);
            if (!errorMessage.isEmpty()) {
                queueMessage(errorMessage);
            }
        }
        if (nullValuesInReview(reviewDTO)) {
            logger.error(REQUIRED_BLANK_FIELDS);
            if (!errorMessage.isEmpty()) {
                queueMessage(errorMessage);
            }
        }
        if (reviewDTO.getRating() <= 0 || reviewDTO.getRating() >= 6) {
           logger.error(QUALIFICATION_OUTSIDE_RANGE);
            if (!errorMessage.isEmpty()) {
                queueMessage(errorMessage);
            }
        }
        if (reviewDTO.getStatus() < 0 || reviewDTO.getStatus() >= 3) {
            logger.error(STATUS_OUTSIDE_RANGE);
            if (!errorMessage.isEmpty()) {
                queueMessage(errorMessage);
            }
        }
        if (reviewDTO.getFeeling()< 0 || reviewDTO.getFeeling() >= 3) {
            logger.error(FEELING_OUTSIDE_RANGE);
            if (!errorMessage.isEmpty()) {
                queueMessage(errorMessage);
            }
        }   
        reviewDTO.setCreatedDate(new Date());
        try {
            reviewWriteDAO.add(reviewDTO);
            if (ReviewStatus.fromInteger(reviewDTO.getStatus()) == ReviewStatus.APPROVED) {
                String successMessage = this.createMessageReviewOk(reviewDTO).toString();
                if (!successMessage.isEmpty()) {
                    this.queueMessage(successMessage);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
    
    public StringBuilder createMessageReviewError(ReviewDTO review) {
        StringBuilder message = new StringBuilder();
        ClientDTO client = findClientByRemoteId(review.getClientId());
        if (client != null) {
            message.append(client.getEmail());
            message.append(";!");
            message.append("Unsuccessful Review");
            message.append(";*");
            message.append("An error occurred in the process of creating the review");
        }
        return message;
    }
    
    public StringBuilder createMessageReviewOk(ReviewDTO review) {
        StringBuilder message = new StringBuilder();
        ClientDTO client = findClientByRemoteId(review.getClientId());
        if (client != null) {
            message.append(client.getEmail());
            message.append(";!");
            message.append("Successful review");
            message.append(";*");
            message.append("Review performed successfully");
        }
        return message;
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
        
   
    private boolean nullValuesInReview(ReviewDTO reviewDTO) {
        return reviewDTO.getComment() == null || reviewDTO.getShipmentId() == null || reviewDTO.getClientId() == null;
    }
    
    private ClientDTO findClientByRemoteId(Long id) {
        ClientDTO clientReturn = null;
        URL url;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/Clients-war/client/");
            urlBuilder.append(id.toString());
            url = new URL(urlBuilder.toString());
            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) url.openConnection();
                try {
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    try {
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } catch (IOException ex) {
                            logger.error(ex.getMessage());
                        }
                        String output = reader != null ? reader.readLine() : null;
                        if (output != null) {
                            ClientDTO clienteDTO = gson.fromJson(output, ClientDTO.class);
                            clientReturn = clienteDTO;
                        }
                        conn.disconnect();
                    } catch (IOException ex) {
                        logger.error(ex.getMessage());
                    }
                } catch (ProtocolException ex) {
                    logger.error(ex.getMessage());
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage());
        }
        return clientReturn;
    }
    

}
