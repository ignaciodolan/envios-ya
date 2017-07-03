package com.enviosya.shipments.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.shipments.dao.ShipmentDAO;
import com.enviosya.shipments.domain.PackageNormal;
import com.enviosya.shipments.dto.CadetDTO;
import com.enviosya.shipments.dto.InitialShipmentDTO;
import com.enviosya.shipments.dto.ClientDTO;
import com.enviosya.shipments.dto.ShipmentDTO;
import com.enviosya.shipments.entities.ShipmentEntity;
import com.enviosya.shipments.exceptions.CadetDistanceException;
import com.enviosya.shipments.exceptions.CalculateCostException;
import com.enviosya.shipments.exceptions.RequestException;
import com.enviosya.shipments.exceptions.ShipmentException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.HttpURLConnection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class ShipmentBean {
    
    @EJB
    private ShipmentDAO shipmentDAO;
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ShipmentBean.class);
    
    private final Gson gson = new Gson();
    
    private static final String URL_PACKAGE_DIMENSIONS = "https://ort-arqsoftort-sizer.herokuapp.com/dimensions";
    
    private static final String URL_CADETS = "http://localhost:8080/Cadet-war/cadet/nearby/{latitude}/{length}";
    
    private static final String URL_CLIENTS = "http://localhost:8080/Clients-war/client/{id}";
    
    private static final String URL_GET_CADET = "http://localhost:8080/Cadet-war/cadet/{id}";
    
    private static final String URL_REVIEW_CADET = "http://localhost:8080/Review-war/review/";
    
    private static final String URL_QUEUE_NOTIFICATION = "http://localhost:8080/Notifications-war/notification";
    
    private static final String SUCCESSFUL_OPERATION = " Operation completed successfully";
    
    private static final String NULL_ENTITY = " Entity to build is null";
    
    private static final String INVALID_CLIENT = " Invalid clients";
    
    private static final String REQUIRED_BLANK_FIELDS
            = " The operation could not be completed because there are required fields empty";

    private static final String NO_SEARCH_RESULTS = " No results were found in the search";
    
    private static final String NOT_AUTHENTICATED_USER = " Invalid user or token";
    
    private static final String LATITUDE = "100";
    
    private static final String LENGTH = "100";
    
    private static final int STATUS_SENT = 0;
    
    private static final int STATUS_RECEIVED = 1;

    public InitialShipmentDTO create(ShipmentDTO shipmentDTO) throws ShipmentException, Exception {
        List<Long> cadetsId = null;
        InitialShipmentDTO initialShipmentDTO = null;
        if(shipmentDTO == null){
            throw new ShipmentException(NULL_ENTITY);
        }
        if(nullValuesInShipmentExist(shipmentDTO)){
            throw new ShipmentException(REQUIRED_BLANK_FIELDS);
        }
        try {
            Double cost = null;
            
            cost = calculateShipmentCost(shipmentDTO.getPackagePhoto());
            shipmentDTO.setCost(cost);
            
            try { 
                
                boolean clientSenderExists = clientExists(shipmentDTO.getClientSender());
                boolean clientReceiverExists = clientExists(shipmentDTO.getClientReceiver());
                if(!clientSenderExists || !clientReceiverExists){
                    StringBuilder message = new StringBuilder();
                    message.append(INVALID_CLIENT);
                    logger.error(message.toString());
                    throw new ShipmentException(message.toString());     
                }
            } catch (Exception exception){
                StringBuilder message = new StringBuilder();
                message.append(INVALID_CLIENT);
                message.append(exception.getMessage());
                logger.error(message.toString());
                throw new ShipmentException(message.toString());                
            }
            
            try { 
                
                cadetsId = getCadetsByDistance();
                
            } catch (CadetDistanceException exception){
                StringBuilder message = new StringBuilder();
                message.append("There was a problem getting the cadets");
                message.append(exception.getMessage());
                logger.error(message.toString());
                throw new ShipmentException(message.toString());                
            }
            
            //Falta calcular comision (?)
            
            shipmentDTO = shipmentDAO.create(shipmentDTO);
            initialShipmentDTO = new InitialShipmentDTO(shipmentDTO);
            initialShipmentDTO.setCadetList(cadetsId);
            
            
        } catch (CalculateCostException exception) {
            StringBuilder message = new StringBuilder();
            message.append(exception.getMessage());
            logger.error(message.toString());
            throw new ShipmentException(message.toString());
        }
        
        return initialShipmentDTO;
    }
    
    private boolean nullValuesInShipmentExist(ShipmentDTO shipmentDTO) {
        return shipmentDTO.getDescription() == null || shipmentDTO.getAddressReceiver() == null 
                || shipmentDTO.getAddressSender() == null  || shipmentDTO.getPackagePhoto()  == null;
    }
    
    
    
    private Double calculateShipmentCost(String packagePhoto) throws CalculateCostException, Exception {
        PackageNormal normalPackage;
        String dimensions;
        Double cost = null;
       
        if(packagePhoto == null){
            throw new CalculateCostException("Package photo can not be null");
        }
        try {
            packagePhoto = "{\"image\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAMAAADXqc3KAAAB+FBMVEUAAAA/mUPidDHiLi5Cn0XkNTPmeUrkdUg/m0Q0pEfcpSbwaVdKskg+lUP4zA/iLi3msSHkOjVAmETdJSjtYFE/lkPnRj3sWUs8kkLeqCVIq0fxvhXqUkbVmSjwa1n1yBLepyX1xxP0xRXqUkboST9KukpHpUbuvRrzrhF/ljbwaljuZFM4jELaoSdLtElJrUj1xxP6zwzfqSU4i0HYnydMtUlIqUfywxb60AxZqEXaoifgMCXptR9MtklHpEY2iUHWnSjvvRr70QujkC+pUC/90glMuEnlOjVMt0j70QriLS1LtEnnRj3qUUXfIidOjsxAhcZFo0bjNDH0xxNLr0dIrUdmntVTkMoyfL8jcLBRuErhJyrgKyb4zA/5zg3tYFBBmUTmQTnhMinruBzvvhnxwxZ/st+Ktt5zp9hqota2vtK6y9FemNBblc9HiMiTtMbFtsM6gcPV2r6dwroseLrMrbQrdLGdyKoobKbo3Zh+ynrgVllZulTsXE3rV0pIqUf42UVUo0JyjEHoS0HmsiHRGR/lmRz/1hjqnxjvpRWfwtOhusaz0LRGf7FEfbDVmqHXlJeW0pbXq5bec3fX0nTnzmuJuWvhoFFhm0FtrziBsjaAaDCYWC+uSi6jQS3FsSfLJiTirCOkuCG1KiG+wSC+GBvgyhTszQ64Z77KAAAARXRSTlMAIQRDLyUgCwsE6ebm5ubg2dLR0byXl4FDQzU1NDEuLSUgC+vr6urq6ubb29vb2tra2tG8vLu7u7uXl5eXgYGBgYGBLiUALabIAAABsElEQVQoz12S9VPjQBxHt8VaOA6HE+AOzv1wd7pJk5I2adpCC7RUcHd3d3fXf5PvLkxheD++z+yb7GSRlwD/+Hj/APQCZWxM5M+goF+RMbHK594v+tPoiN1uHxkt+xzt9+R9wnRTZZQpXQ0T5uP1IQxToyOAZiQu5HEpjeA4SWIoksRxNiGC1tRZJ4LNxgHgnU5nJZBDvuDdl8lzQRBsQ+s9PZt7s7Pz8wsL39/DkIfZ4xlB2Gqsq62ta9oxVlVrNZpihFRpGO9fzQw1ms0NDWZz07iGkJmIFH8xxkc3a/WWlubmFkv9AB2SEpDvKxbjidN2faseaNV3zoHXvv7wMODJdkOHAegweAfFPx4G67KluxzottCU9n8CUqXzcIQdXOytAHqXxomvykhEKN9EFutG22p//0rbNvHVxiJywa8yS2KDfV1dfbu31H8jF1RHiTKtWYeHxUvq3bn0pyjCRaiRU6aDO+gb3aEfEeVNsDgm8zzLy9egPa7Qt8TSJdwhjplk06HH43ZNJ3s91KKCHQ5x4sw1fRGYDZ0n1L4FKb9/BP5JLYxToheoFCVxz57PPS8UhhEpLBVeAAAAAElFTkSuQmCC\"}";

            dimensions = this.sendPost(URL_PACKAGE_DIMENSIONS, packagePhoto);

            if(dimensions == null){
                throw new CalculateCostException("Dimensions can not be null");
            }

            /*
                If in the future a new way to calculate cost, just extend abstract class Package and 
                implement getCost method with new rules
                TODO: Implement remote interfaces 
            */

            normalPackage = gson.fromJson(dimensions, PackageNormal.class);

            cost = normalPackage.getCost();
            
        } catch(RequestException rex){
            throw new CalculateCostException(rex.getMessage());
        }
        
        
        return  cost;
    }
    
    private List<Long> getCadetsByDistance() throws CadetDistanceException, Exception{
        List<Long> cadetsIdList = new ArrayList<Long>();
        String urlNearbyCadets = URL_CADETS;
        urlNearbyCadets = urlNearbyCadets.replace("{latitude}", LATITUDE);
        urlNearbyCadets = urlNearbyCadets.replace("{length}", LENGTH);
        String cadetsJson = this.sendGet(urlNearbyCadets);
        Type listCadetDTOType = new TypeToken<List<CadetDTO>>(){}.getType();
        List<CadetDTO> cadetList = (List<CadetDTO>) gson.fromJson(cadetsJson, listCadetDTOType);
        
        if(cadetList.isEmpty()){
            throw new CadetDistanceException("There are no cadets available");
        }
        for (CadetDTO cadet : cadetList) {
            
            cadetsIdList.add(cadet.getId());
        }
        
        if(cadetsIdList.isEmpty()){
            throw new CadetDistanceException("There are no cadets available");
        }
        
        return cadetsIdList;
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
    
    // HTTP POST request
    private String sendPost(String url, String parameters) throws Exception, RequestException {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json");

            String urlParameters = parameters;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

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

    public ShipmentDTO addCadetToShipment(Long shipmentId, Long cadetId) throws ShipmentException, IOException {
        if(shipmentId == null){
            throw new ShipmentException(NULL_ENTITY);
        }
        if(cadetId == null){
            throw new ShipmentException(NULL_ENTITY);
        }
        ShipmentDTO shipmentDTO = shipmentDAO.addCadetToShipment(shipmentId, cadetId);
        // Getting Cadet information
        try {
            String urlCadet = URL_GET_CADET;
            urlCadet = urlCadet.replace("{id}", shipmentDTO.getCadet().toString());
            String cadetJSON = this.sendGet(urlCadet);
            CadetDTO cadetDTO = gson.fromJson(cadetJSON, CadetDTO.class);

            //Email (Notification) for cadet
            StringBuilder messageForCadet = new StringBuilder();
            messageForCadet.append("<start-email>]");
            messageForCadet.append(cadetDTO.getEmail());
            messageForCadet.append("<end-email>");
            messageForCadet.append("<start-subject>");
            messageForCadet.append("New package to deliver");
            messageForCadet.append("<end-subject>");
            messageForCadet.append("<start-message>");
            messageForCadet.append(" Package info: ");
            messageForCadet.append(shipmentDTO.getDescription());
            messageForCadet.append(" Address: ");
            messageForCadet.append(shipmentDTO.getAddressReceiver());
            messageForCadet.append("<end-message>");
            this.queueNotification(messageForCadet.toString(), "email");
            
        } catch(MalformedURLException | RequestException ex){
            StringBuilder message = new StringBuilder();
            message.append("[URL]");
            message.append(ex.getMessage());
            logger.error(message.toString());
            message.append(" maformed: ");
            throw new ShipmentException(message.toString());
        } catch (Exception ex) {
            StringBuilder message = new StringBuilder();
            message.append(ex.getMessage());
            logger.error(message.toString());
            throw new ShipmentException(message.toString());
        }
        
        
        
        return shipmentDTO;
    }
    
    
    public ShipmentDTO confirmShipmentReceived(Long shipmentId) throws ShipmentException, Exception {
        // Check if the user is logged
        if(shipmentId == null){
            throw new ShipmentException(NULL_ENTITY);
        }
        ShipmentDTO shipmentDTO = shipmentDAO.changeShipmentStatus(shipmentId, STATUS_RECEIVED);
        
        // Getting Cadet information
        String urlCadet = URL_GET_CADET;
        urlCadet = urlCadet.replace("{id}", shipmentDTO.getCadet().toString());
        String cadetJSON = this.sendGet(urlCadet);
        CadetDTO cadetDTO = gson.fromJson(cadetJSON, CadetDTO.class);
        
        // Getting Client sender information
        String urlClientSender = URL_CLIENTS;
        urlClientSender = urlClientSender.replace("{id}", shipmentDTO.getClientSender().toString());
        String clientSenderJSON = this.sendGet(urlClientSender);
        ClientDTO clientSenderDto = gson.fromJson(clientSenderJSON, ClientDTO.class);
        
        // Getting Client reveiver information
        String urlClientReceiver = URL_CLIENTS;
        urlClientReceiver = urlClientReceiver.replace("{id}", shipmentDTO.getClientReceiver().toString());
        String clientReceiverJSON = this.sendGet(urlClientReceiver);
        ClientDTO clientReceiverDto = gson.fromJson(clientReceiverJSON, ClientDTO.class);
        
        if (clientSenderDto == null || clientSenderDto == null || cadetDTO == null) {
            throw new ShipmentException(NULL_ENTITY);
        }
        //Email (Notification) for client sender
        StringBuilder messageSender = new StringBuilder();
        messageSender.append("<start-email>]");
        messageSender.append(clientSenderDto.getEmail());
        messageSender.append("<end-email>");
        messageSender.append("<start-subject>");
        messageSender.append("Your package has been sent");
        messageSender.append("<end-subject>");
        messageSender.append("<start-message>");
        messageSender.append(clientSenderDto.getName());
        messageSender.append(clientSenderDto.getLastName());
        messageSender.append(" Your package arrived, was delivered by: ");
        messageSender.append(cadetDTO.getFullName());
        messageSender.append(" Please review: ");
        messageSender.append(URL_REVIEW_CADET);
        messageSender.append("<end-message>");
        
        this.queueNotification(messageSender.toString(), "email");
        
        //Email (Notification) for client receiver
        StringBuilder messageForReceiver = new StringBuilder();
        messageForReceiver.append("<start-email>]");
        messageForReceiver.append(clientReceiverDto.getEmail());
        messageForReceiver.append("<end-email>");
        messageForReceiver.append("<start-subject>");
        messageForReceiver.append("Your package arrived");
        messageForReceiver.append("<end-subject>");
        messageForReceiver.append("<start-message>");
        messageForReceiver.append(clientReceiverDto.getName());
        messageForReceiver.append(clientReceiverDto.getLastName());
        messageForReceiver.append(" was delivered from: ");
        messageForReceiver.append(clientSenderDto.getName());
        messageForReceiver.append(clientSenderDto.getLastName());
        messageForReceiver.append(". Delivered by: ");
        messageForReceiver.append(cadetDTO.getFullName());
        messageSender.append(" Please review: ");
        messageSender.append(URL_REVIEW_CADET);
        messageForReceiver.append("<end-message>");
        
        this.queueNotification(messageForReceiver.toString(), "email");
        
        return shipmentDTO;
    }
    
    public void queueNotification(String message, String via) throws Exception{
        String parameters = "{\"message\":\"{message}\", \"type\":\"{type}\"}";
        parameters = parameters.replace("{message}", message);
        parameters = parameters.replace("{type}", via);
        this.sendPost(URL_QUEUE_NOTIFICATION, parameters);
    }

    private boolean clientExists(Long clientSender) throws Exception {
        String urlClient = URL_CLIENTS;
        urlClient = urlClient.replace("{id}", clientSender.toString());
        String clientSenderJSON = this.sendGet(urlClient);
        ClientDTO client = gson.fromJson(clientSenderJSON, ClientDTO.class);
        return client != null;
    }

    public List<ShipmentDTO> getShipmentsByCadet(Long cadetId) throws ShipmentException{
        // Chequear si esta logueado
        try {
        
            List<ShipmentEntity> shipmentEntityList = shipmentDAO.getShipmentListFromCadetId(cadetId);
            
            List<ShipmentDTO> shipmentDTOList = new ArrayList<ShipmentDTO>();

            shipmentEntityList.forEach((shipment) -> {
                shipmentDTOList.add(shipmentDAO.toDTO(shipment));
            });
            
            return shipmentDTOList;

        } catch (Exception e) {
            
            throw new ShipmentException(e.getMessage());
        
        }
    }

}
