/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.notifications.resources;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.notifications.beans.NotificationBean;
import com.enviosya.notifications.dto.MessageDTO;
import com.enviosya.notifications.exceptions.NotificationException;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * REST Web Service
 *
 * @author ignaciodolan
 */
@Path("notification")
public class NotificationResource {

    @Context
    private UriInfo context;
    
    @EJB
    private NotificationBean notificationBean;
    
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(NotificationResource.class);
    
    /**
     * Creates a new instance of NotificationResource
     */
    public NotificationResource() {
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotification(String messageJSON){ 
        Response response;
        StringBuilder message;
        try {
            MessageDTO messageDTO = gson.fromJson(messageJSON, MessageDTO.class);
            notificationBean.sendMessage(messageDTO);
            message = new StringBuilder();
            message.append("Message Received: ");
            message.append(gson.toJson(messageDTO));
            logger.success(message.toString());
            response = Response.status(Response.Status.OK).entity(message.toString()).build();
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
        } catch (NotificationException e) {
            message = new StringBuilder();
            message.append("[NotificationException]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .build();
        } catch (Exception ex) {
            message = new StringBuilder();
            message.append("[Exception]");
            message.append(ex.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        }
        return response;
    }
    
    /**
     * Retrieves representation of an instance of com.enviosya.notifications.resources.NotificationResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of NotificationResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
