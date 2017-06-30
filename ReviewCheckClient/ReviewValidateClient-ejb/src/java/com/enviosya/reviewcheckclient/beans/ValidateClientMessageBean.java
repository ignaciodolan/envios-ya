
package com.enviosya.reviewcheckclient.beans;

import com.enviosya.reviewcheckclient.dto.ReviewDTO;
import com.enviosya.reviewcheckclient.dto.ShipmentDTO;
import com.google.gson.Gson;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.reviewcheckclient.dto.ClientDTO;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(
            propertyName = "destinationLookup", 
            propertyValue = "jms/QueueValidateClient"
    ),
    @ActivationConfigProperty(
            propertyName = "destinationType", 
            propertyValue = "javax.jms.Queue"
    )
})
public class ValidateClientMessageBean implements MessageListener {
    
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ValidateClientMessageBean.class);
    @Resource(lookup = "jms/QueueCheckDuplicates")
    private Queue queueNoDuplicatedReview;

    public ValidateClientMessageBean() {
    }
    
    
    @Override
    public void onMessage(Message message) {
        try {
            ReviewDTO reviewDTO = (ReviewDTO) ((ObjectMessage) message).getObject();
            validateClient(reviewDTO);
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    private void validateClient(ReviewDTO reviewDTO) {
        ShipmentDTO shipmentDTO = findShipmentByRemoteId(reviewDTO.getShipmentId());
        if (shipmentDTO != null) {
            if (Objects.equals(shipmentDTO.getClientSender(), reviewDTO.getClientId())
                || Objects.equals(shipmentDTO.getClientReceiver(), reviewDTO.getClientId())) {
                queueReview(reviewDTO);
            } else {
                String message = createMessageForClient(reviewDTO).toString();
                if (!message.isEmpty()) {
                    queueMessage(message);
                }
            }
        }
    }
    
    private ShipmentDTO findShipmentByRemoteId(Long id) {
        ShipmentDTO shipmentReturn = null;
        URL url;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/Shipments-war/shipment/{id}");
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
                            ShipmentDTO envioDto = gson.fromJson(output, ShipmentDTO.class);
                            shipmentReturn = envioDto;
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
        return shipmentReturn;
    }    
    
    private void queueReview(ReviewDTO review) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            ObjectMessage msg = session.createObjectMessage(review);
            MessageProducer producer = session.createProducer(queueNoDuplicatedReview);
            producer.send(msg);
            session.close();
            connection.close();
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
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
    
    public StringBuilder createMessageForClient(ReviewDTO reviewDTO) {
        StringBuilder message = new StringBuilder();
        ClientDTO clientDTO = findClientByRemoteId(reviewDTO.getClientId());
        if (clientDTO != null) {
            message.append("<start-email>]");
            message.append(clientDTO.getEmail());
            message.append("<end-email>");
            message.append("<start-subject>");
            message.append("Invalid Client");
            message.append("<end-subject>");
            message.append("<start-message>");
            message.append("The client ");
            message.append(clientDTO.getName());
            message.append(clientDTO.getLastName());
            message.append(" didnt create this review");
            message.append("<end-message>");
            
        }
        return message;
    }
    
    private ClientDTO findClientByRemoteId(Long id) {
        ClientDTO clientReturn = null;
        URL url;
        try {
            //corroborar url
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
