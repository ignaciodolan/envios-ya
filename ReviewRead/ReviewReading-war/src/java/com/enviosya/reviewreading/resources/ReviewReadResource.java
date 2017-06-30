
package com.enviosya.reviewreading.resources;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.reviewread.beans.ReviewReadBean;
import com.enviosya.reviewread.dto.ReviewDTO;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("reviewReading")
public class ReviewReadResource {

    @Context
    private UriInfo context;

    @EJB
    private ReviewReadBean reviewReadBean;
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ReviewReadResource.class);
    
    public ReviewReadResource() {
    }

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviews() {
        StringBuilder message;
        Response response;
        try {
            List<ReviewDTO> reviews = reviewReadBean.getReviews();
            response = Response.status(Response.Status.OK).entity(gson.toJson(reviews)).build();
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }
    
    @GET
    @Path("/exists/{shipmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByShipmentId(@PathParam("shipmentId") Long shipmentId) {
        StringBuilder message;
        Response response;
        try {
            ReviewDTO review = reviewReadBean.getReviewByShipmentId(shipmentId);
            response = Response.status(Response.Status.OK).entity(gson.toJson(review)).build();
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        
        return response;
    }
    
    /**
     * Retrieves representation of an instance of com.enviosya.resources.ReviewReadingResource
     * @param status
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/status/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByStatus(@PathParam("status") int status) {
        StringBuilder message;
        Response response;
        try {
            List<ReviewDTO> clients = reviewReadBean.getReviewsByStatus(status);
            response = Response.status(Response.Status.OK).entity(gson.toJson(clients)).build();
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        
        return response;
    }
    
    /**
     * Retrieves representation of an instance of com.enviosya.resources.ReviewReadingResource
     * @param status
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/cadet/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsFromCadet(@PathParam("id") Long id) {
        StringBuilder message;
        Response response;
        try {
            List<ReviewDTO> clients = reviewReadBean.getReviewsApprovedFromCadet(id);
            response = Response.status(Response.Status.OK).entity(gson.toJson(clients)).build();
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        
        return response;
    }
    
        
    /**
     * Retrieves representation of an instance of com.enviosya.resources.ReviewReadingResource
     * @param status
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewById(@PathParam("id") Long id) {
        StringBuilder message;
        Response response;
        try {
            ReviewDTO review = reviewReadBean.getReviewById(id);
            response = Response.status(Response.Status.OK).entity(gson.toJson(review)).build();
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        
        return response;
    }
    
}
