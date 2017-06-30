
package com.enviosya.reviewread.beans;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.reviewread.dto.CadetDTO;
import com.enviosya.reviewread.dao.ReviewReadDAO;
import com.enviosya.reviewread.dto.CadetReviewDTO;
import com.enviosya.reviewread.dto.ReviewDTO;
import com.enviosya.reviewread.dto.ShipmentDTO;
import com.enviosya.reviewread.entities.ReviewEntity;
import com.enviosya.reviewread.exceptions.RequestException;
import com.enviosya.reviewread.exceptions.ReviewReadException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


@Stateless
@LocalBean
public class ReviewReadBean {

    @EJB
    private ReviewReadDAO reviewDAO;

    private final Gson gson = new Gson();
    
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ReviewReadBean.class);

    private static final String NO_REVIEWS_FOR_THAT_SHIPPING = " There are no reviews associated to that shipment";

    private static final String NO_SEARCH_RESULTS = " No search results";
    
    private static final String URL_GET_SHIPMENT = "http://localhost:8080/Shipments-war/shipment/cadet/{id}";
    
    private static final String URL_GET_CADET = "http://localhost:8080/Cadet-war/cadet/{id}";
    
    @Context 
    private HttpServletRequest request;

    private List<ShipmentDTO> getShipmentByCadet(Long cadetId) throws MalformedURLException, IOException, RequestException { 
        List<ShipmentDTO> shipmentReturn;
        String urlGetShipments = URL_GET_SHIPMENT;
        urlGetShipments = urlGetShipments.replace("{id}", cadetId.toString());
        String shipmentJson = sendGet(urlGetShipments);
        Type listShipmentDTOType = new TypeToken<List<ShipmentDTO>>(){}.getType();
        shipmentReturn = (List<ShipmentDTO>) gson.fromJson(shipmentJson, listShipmentDTOType);
        return shipmentReturn;
    }
    
    
    private CadetDTO getCadetById(Long cadetId) throws ProtocolException, IOException, MalformedURLException, RequestException {
        CadetDTO cadetReturn;
        String urlGetCadet = URL_GET_CADET;
        urlGetCadet = urlGetCadet.replace("{id}", cadetId.toString());
        String cadetJson = sendGet(urlGetCadet);
        cadetReturn = gson.fromJson(cadetJson, CadetDTO.class);
        return cadetReturn;
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
    

    public List<ReviewDTO> getReviews() {
        // Chequear si esta logueado
        try {
        
            List<ReviewEntity> reviewsEntity = reviewDAO.getReviews();
            
            List<ReviewDTO> reviewsDTO = new ArrayList<ReviewDTO>();

            reviewsEntity.forEach((review) -> {
                reviewsDTO.add(reviewDAO.toDTO(review));
            });
            
            return reviewsDTO;

        } catch (Exception e) {
            
            throw e;
        
        }
    }

    public List<ReviewDTO> getReviewsByStatus(int status) {
        List<ReviewDTO> reviewsDTO = this.getReviews();
        List<ReviewDTO> reviewsDTOByStatus = this.getReviews();
        reviewsDTO.forEach((ReviewDTO review) -> {
            if(review.getStatus() == (status)){
               reviewsDTO.add(review);
            }
        });
        return reviewsDTOByStatus;
    }

    public List<ReviewDTO> getReviewsApprovedFromCadet(Long id) throws IOException, MalformedURLException, RequestException {
        List<ShipmentDTO> shipmentList = getShipmentByCadet(id);
        List<ReviewDTO> reviewsDTO = new ArrayList<ReviewDTO>();

        shipmentList.forEach((shipment) -> {
            ReviewDTO reviewWithShipment;
            try {
                reviewWithShipment = getReviewByShipmentId(shipment.getId());
                if(reviewWithShipment.getStatus() == 1){
                    reviewsDTO.add(reviewWithShipment);
                }
            } catch (ReviewReadException ex) {
                logger.error(ex.getMessage());
            }
        });

        return reviewsDTO;
    }

    public ReviewDTO getReviewByShipmentId(Long shipmentId) throws ReviewReadException {
        //chequear si el usuario esta logueado
        if(shipmentId == null){
            throw new ReviewReadException("ID is null");
        }
        
        List<ReviewEntity> reviewEntityList = reviewDAO.findByShipmentsId(shipmentId);
        
        if(reviewEntityList.isEmpty()){
            
            throw new ReviewReadException("Review doesnt exist");    
            
        }
        ReviewEntity reviewEntity = reviewEntityList.get(0);
        ReviewDTO reviewDTO = reviewDAO.toDTO(reviewEntity);
        
        return reviewDTO;
    }

    public ReviewDTO getReviewById(Long id) throws ReviewReadException {
        //chequear si el usuario esta logueado
        if(id == null){
            throw new ReviewReadException("ID is null");
        }
        
        ReviewEntity reviewEntity = reviewDAO.find(id);
        
        if(reviewEntity == null){
            
            throw new ReviewReadException("Review doesnt exist");    
            
        }
        
        ReviewDTO reviewDTO = reviewDAO.toDTO(reviewEntity);
        
        return reviewDTO;
    }
    
}
