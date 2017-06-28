package com.enviosya.shipments.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.shipments.dao.ShipmentDAO;
import com.enviosya.shipments.domain.PackageNormal;
import com.enviosya.shipments.dto.CadetDTO;
import com.enviosya.shipments.dto.InitialShipmentDTO;
import com.enviosya.shipments.dto.ShipmentDTO;
import com.enviosya.shipments.exceptions.CadetDistanceException;
import com.enviosya.shipments.exceptions.CalculateCostException;
import com.enviosya.shipments.exceptions.ShipmentException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

@Stateless
@LocalBean
public class ShipmentBean {
    
    @EJB
    private ShipmentDAO shipmentDAO;
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ShipmentBean.class);
    
    private final Gson gson = new Gson();
    
    private static final String URL_PACKAGE_DIMENSIONS = "https://ort-arqsoftort-sizer.herokuapp.com/dimensions";
    
    private static final String URL_CADETS = "http://localhost:8080/Cadet-war/cadet/nearby/{latitude}/{length}";
    
    private static final String SUCCESSFUL_OPERATION = " Operation completed successfully";
    
    private static final String NULL_ENTITY = " Entity to build is null";
    
    
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
            message.append("There was a problem calculating the package cost");
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
        
        packagePhoto = "{\"image\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAMAAADXqc3KAAAB+FBMVEUAAAA/mUPidDHiLi5Cn0XkNTPmeUrkdUg/m0Q0pEfcpSbwaVdKskg+lUP4zA/iLi3msSHkOjVAmETdJSjtYFE/lkPnRj3sWUs8kkLeqCVIq0fxvhXqUkbVmSjwa1n1yBLepyX1xxP0xRXqUkboST9KukpHpUbuvRrzrhF/ljbwaljuZFM4jELaoSdLtElJrUj1xxP6zwzfqSU4i0HYnydMtUlIqUfywxb60AxZqEXaoifgMCXptR9MtklHpEY2iUHWnSjvvRr70QujkC+pUC/90glMuEnlOjVMt0j70QriLS1LtEnnRj3qUUXfIidOjsxAhcZFo0bjNDH0xxNLr0dIrUdmntVTkMoyfL8jcLBRuErhJyrgKyb4zA/5zg3tYFBBmUTmQTnhMinruBzvvhnxwxZ/st+Ktt5zp9hqota2vtK6y9FemNBblc9HiMiTtMbFtsM6gcPV2r6dwroseLrMrbQrdLGdyKoobKbo3Zh+ynrgVllZulTsXE3rV0pIqUf42UVUo0JyjEHoS0HmsiHRGR/lmRz/1hjqnxjvpRWfwtOhusaz0LRGf7FEfbDVmqHXlJeW0pbXq5bec3fX0nTnzmuJuWvhoFFhm0FtrziBsjaAaDCYWC+uSi6jQS3FsSfLJiTirCOkuCG1KiG+wSC+GBvgyhTszQ64Z77KAAAARXRSTlMAIQRDLyUgCwsE6ebm5ubg2dLR0byXl4FDQzU1NDEuLSUgC+vr6urq6ubb29vb2tra2tG8vLu7u7uXl5eXgYGBgYGBLiUALabIAAABsElEQVQoz12S9VPjQBxHt8VaOA6HE+AOzv1wd7pJk5I2adpCC7RUcHd3d3fXf5PvLkxheD++z+yb7GSRlwD/+Hj/APQCZWxM5M+goF+RMbHK594v+tPoiN1uHxkt+xzt9+R9wnRTZZQpXQ0T5uP1IQxToyOAZiQu5HEpjeA4SWIoksRxNiGC1tRZJ4LNxgHgnU5nJZBDvuDdl8lzQRBsQ+s9PZt7s7Pz8wsL39/DkIfZ4xlB2Gqsq62ta9oxVlVrNZpihFRpGO9fzQw1ms0NDWZz07iGkJmIFH8xxkc3a/WWlubmFkv9AB2SEpDvKxbjidN2faseaNV3zoHXvv7wMODJdkOHAegweAfFPx4G67KluxzottCU9n8CUqXzcIQdXOytAHqXxomvykhEKN9EFutG22p//0rbNvHVxiJywa8yS2KDfV1dfbu31H8jF1RHiTKtWYeHxUvq3bn0pyjCRaiRU6aDO+gb3aEfEeVNsDgm8zzLy9egPa7Qt8TSJdwhjplk06HH43ZNJ3s91KKCHQ5x4sw1fRGYDZ0n1L4FKb9/BP5JLYxToheoFCVxz57PPS8UhhEpLBVeAAAAAElFTkSuQmCC\"}";
        
//        dimensions = this.getJsonFromAPI(URL_PACKAGE_DIMENSIONS,"POST", packagePhoto);
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
        
        return  cost;
    }

    private List<Long> getCadetsByDistance() throws CadetDistanceException, Exception{
        List<Long> cadetsIdList = null;
        String urlNearbyCadets = URL_CADETS;
        urlNearbyCadets = urlNearbyCadets.replace("{latitude}", LATITUDE);
        urlNearbyCadets = urlNearbyCadets.replace("{length}", LENGTH);
        String cadetsJson = this.sendGet(urlNearbyCadets, null);
        Type listCadetDTOType = new TypeToken<List<CadetDTO>>(){}.getType();
        List<CadetDTO> cadetList = (List<CadetDTO>) gson.fromJson(cadetsJson, listCadetDTOType);
        
        for (CadetDTO cadet : cadetList) {
            cadetsIdList.add(cadet.getId());
        }
        
        if(cadetsIdList.isEmpty()){
            throw new CadetDistanceException("There are no cadets available");
        }
        
        return cadetsIdList;
    }
    
    // HTTP GET request
    private String sendGet(String url, String parameters) throws Exception {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            
            // TODO throw exception dependiendo el response code!
            
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
    private String sendPost(String url, String parameters) throws Exception {

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
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

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

    public ShipmentDTO addCadetToShipment(Long shipmentId, Long cadetId) throws ShipmentException {
        if(shipmentId == null){
            throw new ShipmentException(NULL_ENTITY);
        }
        if(cadetId == null){
            throw new ShipmentException(NULL_ENTITY);
        }
        ShipmentDTO shipmentDTO = shipmentDAO.addCadetToShipment(shipmentId, cadetId); 
        return shipmentDTO;
    }
    
    
    public ShipmentDTO confirmShipmentReceived(Long shipmentId) throws ShipmentException {
        // Check if the user is logged
        if(shipmentId == null){
            throw new ShipmentException(NULL_ENTITY);
        }
        ShipmentDTO shipmentDTO = shipmentDAO.changeShipmentStatus(shipmentId, STATUS_RECEIVED);
        //ENCOLAR MENSAJE!! NOTIFIACIONES !!
        return shipmentDTO;
    }

}
