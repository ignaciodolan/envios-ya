/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.review.resources;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.review.beans.ReviewBean;
import com.enviosya.review.dto.ReviewDTO;
import com.enviosya.review.exceptions.ReviewException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author ignaciodolan
 */
@Path("review")
public class ReviewResource {

    @Context
    private UriInfo context;


    @EJB
    private ReviewBean reviewBean;
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ReviewResource.class);

    /**
     * Creates a new instance of GenericResource
     */
    public ReviewResource() {
    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReview(String reviewJSON){ 
        Response response;
        StringBuilder message;
        try {
            ReviewDTO reviewDTO = gson.fromJson(reviewJSON, ReviewDTO.class);
            reviewDTO = reviewBean.create(reviewDTO); 
            message = new StringBuilder();
            message.append("Review started: ");
            message.append(gson.toJson(reviewDTO));
            logger.success(message.toString());
            response = Response.status(Response.Status.CREATED).entity(reviewDTO.toString()).build();
        } catch (JsonSyntaxException e) {
            message = new StringBuilder();
            message.append("[Mensaje Syntax error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Mensaje IO error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        } catch (ReviewException e) {
            message = new StringBuilder();
            message.append("[ReviewException]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        return response;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviews() {
        StringBuilder message;
        Response response;
        try {
            List<ReviewDTO> reviews = reviewBean.getReviewList();
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewById(@PathParam("id") Long id) {
        StringBuilder message;
        Response response;
        try {
            ReviewDTO reviewDTO = reviewBean.getReviewById(id);
            response = Response.status(Response.Status.OK).entity(gson.toJson(reviewDTO)).build();
        } catch (ReviewException e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }
    
    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
