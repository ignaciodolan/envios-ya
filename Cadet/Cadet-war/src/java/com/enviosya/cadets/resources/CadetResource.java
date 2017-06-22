/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.cadets.resources;

import com.enviosya.cadets.beans.CadetBean;
import com.enviosya.cadets.dto.CadetDTO;
import com.enviosya.cadets.exceptions.CadetException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 * 
 */
@Path("cadet")
public class CadetResource {

    @EJB
    private CadetBean cadetBean;
    
    private final Gson gson = new Gson();
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CadetResource
     */
    
    public CadetResource() {
    }

    /**
     * Retrieves representation of an instance of com.enviosya.cadet.resource.CadetResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCadet(
            String cadetJSON
    ) {
        Response response;
        StringBuilder message;
        try {
            CadetDTO cadetDTO = gson.fromJson(cadetJSON, CadetDTO.class);
            cadetDTO = cadetBean.create(cadetDTO); 
            message = new StringBuilder();
            message.append("Se creo exitosamente el cadete: ");
//            TODO: Add log here
//            log.success(message);
            response = Response.status(Response.Status.CREATED).entity(gson.toJson(cadetDTO)).build();
            //response =  Response.ok(cadetDTO).build();
        } catch (JsonSyntaxException e) {
            message = new StringBuilder();
            message.append("[Message Syntax error gson]");
            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Message IO error gson]");
            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]");
            message.append(e.getMessage());
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        }
        return response;
    }
    /**
     * PUT method for updating or creating an instance of CadetResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    
}
