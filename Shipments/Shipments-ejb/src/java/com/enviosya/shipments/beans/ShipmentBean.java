package com.enviosya.shipments.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.shipments.dao.ShipmentDAO;
import com.enviosya.shipments.domain.PackageNormal;
import com.enviosya.shipments.dto.CadetDTO;
import com.enviosya.shipments.dto.ShipmentDTO;
import com.enviosya.shipments.entities.ShipmentEntity;
import com.enviosya.shipments.exceptions.CadetDistanceException;
import com.enviosya.shipments.exceptions.CalculateCostException;
import com.enviosya.shipments.exceptions.ShipmentException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.net.URL;
import java.io.BufferedReader;
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
    
    private static final String URL_PACKAGE_DIMENSIONS = "https://ort-arqsoftort-sizer.herokuapp.com/";
    
    private static final String URL_CADETS = "http://localhost:8080/Cadet-war/cadet";
    
    private static final String SUCCESSFUL_OPERATION = " Operation completed successfully";
    
    private static final String NULL_ENTITY = " Entity to build is null";
    
    private static final String REQUIRED_BLANK_FIELDS
            = " The operation could not be completed because there are required fields empty";

    private static final String NO_SEARCH_RESULTS = " No results were found in the search";
    
    private static final String NOT_AUTHENTICATED_USER = " Invalid user or token";
    
    private static final int DISTANCE_RADIO = 100;

    public List<String> create(ShipmentDTO shipmentDTO) throws ShipmentException {
        List<String> cadetsId = null;
        try {
            // Calcular costo
            Double cost = null;
            
            cost = this.calculateShipmentCost(shipmentDTO.getPackagePhoto());
            
            // Agarrar 4 cadetes cercanos
            try { 
                
                cadetsId = this.getCadetsByDistance(4, DISTANCE_RADIO);
                
            } catch (CadetDistanceException exception){
                StringBuilder message = new StringBuilder();
                message.append("There was a problem getting the cadets");
                message.append(exception.getMessage());
                logger.error(message.toString());
                throw new ShipmentException(message.toString());                
            }
            
            // Persistir envio
            
        } catch (CalculateCostException exception) {
            StringBuilder message = new StringBuilder();
            message.append("There was a problem calculating the package cost");
            message.append(exception.getMessage());
            logger.error(message.toString());
            throw new ShipmentException(message.toString());
        }
        
        return cadetsId;
    }
    
    private Double calculateShipmentCost(String packagePhoto) throws CalculateCostException {
        PackageNormal normalPackage;
        String dimensions;
        Double cost = null;
       
        if(packagePhoto == null){
            throw new CalculateCostException("Package photo can not be blank");
        }
        
        packagePhoto = "{\"image\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAMAAADXqc3KAAAB+FBMVEUAAAA/mUPidDHiLi5Cn0XkNTPmeUrkdUg/m0Q0pEfcpSbwaVdKskg+lUP4zA/iLi3msSHkOjVAmETdJSjtYFE/lkPnRj3sWUs8kkLeqCVIq0fxvhXqUkbVmSjwa1n1yBLepyX1xxP0xRXqUkboST9KukpHpUbuvRrzrhF/ljbwaljuZFM4jELaoSdLtElJrUj1xxP6zwzfqSU4i0HYnydMtUlIqUfywxb60AxZqEXaoifgMCXptR9MtklHpEY2iUHWnSjvvRr70QujkC+pUC/90glMuEnlOjVMt0j70QriLS1LtEnnRj3qUUXfIidOjsxAhcZFo0bjNDH0xxNLr0dIrUdmntVTkMoyfL8jcLBRuErhJyrgKyb4zA/5zg3tYFBBmUTmQTnhMinruBzvvhnxwxZ/st+Ktt5zp9hqota2vtK6y9FemNBblc9HiMiTtMbFtsM6gcPV2r6dwroseLrMrbQrdLGdyKoobKbo3Zh+ynrgVllZulTsXE3rV0pIqUf42UVUo0JyjEHoS0HmsiHRGR/lmRz/1hjqnxjvpRWfwtOhusaz0LRGf7FEfbDVmqHXlJeW0pbXq5bec3fX0nTnzmuJuWvhoFFhm0FtrziBsjaAaDCYWC+uSi6jQS3FsSfLJiTirCOkuCG1KiG+wSC+GBvgyhTszQ64Z77KAAAARXRSTlMAIQRDLyUgCwsE6ebm5ubg2dLR0byXl4FDQzU1NDEuLSUgC+vr6urq6ubb29vb2tra2tG8vLu7u7uXl5eXgYGBgYGBLiUALabIAAABsElEQVQoz12S9VPjQBxHt8VaOA6HE+AOzv1wd7pJk5I2adpCC7RUcHd3d3fXf5PvLkxheD++z+yb7GSRlwD/+Hj/APQCZWxM5M+goF+RMbHK594v+tPoiN1uHxkt+xzt9+R9wnRTZZQpXQ0T5uP1IQxToyOAZiQu5HEpjeA4SWIoksRxNiGC1tRZJ4LNxgHgnU5nJZBDvuDdl8lzQRBsQ+s9PZt7s7Pz8wsL39/DkIfZ4xlB2Gqsq62ta9oxVlVrNZpihFRpGO9fzQw1ms0NDWZz07iGkJmIFH8xxkc3a/WWlubmFkv9AB2SEpDvKxbjidN2faseaNV3zoHXvv7wMODJdkOHAegweAfFPx4G67KluxzottCU9n8CUqXzcIQdXOytAHqXxomvykhEKN9EFutG22p//0rbNvHVxiJywa8yS2KDfV1dfbu31H8jF1RHiTKtWYeHxUvq3bn0pyjCRaiRU6aDO+gb3aEfEeVNsDgm8zzLy9egPa7Qt8TSJdwhjplk06HH43ZNJ3s91KKCHQ5x4sw1fRGYDZ0n1L4FKb9/BP5JLYxToheoFCVxz57PPS8UhhEpLBVeAAAAAElFTkSuQmCC\"}";
        
        dimensions = this.getJsonFromAPI(URL_PACKAGE_DIMENSIONS,"POST", packagePhoto);
        
        if(dimensions == null){
            throw new CalculateCostException("It wasnt possible to calculate dimensions");
        }
        
        /* 
            If in the future a new way to calculate cost, just extend abstract class Package and 
            implement getCost method with new rules
        */        
       
        normalPackage = gson.fromJson(dimensions, PackageNormal.class);
        
        cost = normalPackage.getCost();
        
        return  cost;
    }

    private List<String> getCadetsByDistance(int cadetsQuantity, int distanceRadio) throws CadetDistanceException{
        List<String> cadetsIdList = null;
        String cadetsJson = this.getJsonFromAPI(URL_CADETS,"GET",null);
        Type listCadetDTOType = new TypeToken<List<CadetDTO>>(){}.getType();
        List<CadetDTO> cadetList = (List<CadetDTO>) gson.fromJson(cadetsJson, listCadetDTOType);
        
        for (CadetDTO cadet : cadetList) {
            cadetsIdList.add(cadet.getId().toString());
        }
        
        if(cadetsIdList.isEmpty()){
            throw new CadetDistanceException("There are no cadets available");
        }
        return cadetsIdList;
    }
    
    private String getJsonFromAPI(String _url, String method, String request) {
        String response = null;
        URL url = null;
        try {
            url = new URL(_url);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                try {
                    connection.setDoOutput(true);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                    outputStreamWriter.write(request);
                    outputStreamWriter.flush();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("X-Mashape-Key", "<required>");
                    connection.setRequestProperty("Accept", "application/json");

                    try {
                        BufferedReader br = null;
                        try {
                            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        } catch (IOException ex) {
                            logger.error(ex.toString());
                        }
                        String output = br != null ? br.readLine() : null;
                        if (output != null) {
                            response = output;
                        }
                        connection.disconnect();
                    } catch (IOException ex) {
                        logger.error(ex.toString());
                    }
                    connection.disconnect();
                } catch (ProtocolException ex) {
                    logger.error(ex.toString());
                }
            } catch (IOException ex) {
                logger.error(ex.toString());
            }
        } catch (MalformedURLException ex) {
            logger.error(ex.toString());
        }
        return response;
    }
    
}
