/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.cadets.resources;

import com.enviosya.cadets.beans.VehicleBean;
import com.enviosya.cadets.dto.VehicleDTO;
import com.enviosya.cadets.exceptions.VehicleException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author Ruso
 */
@Path("vehicle")

public class VehicleResource {

    @EJB
    VehicleBean vehicleBean;
    
    private final Gson gson = new Gson();
    
    @Context
    private UriInfo context;
    
    public VehicleResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of CadetResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createVehicle(String vehicleJSON) {
        Response response;
        StringBuilder message = null;
        try {
            VehicleDTO vehicleDTO = gson.fromJson(vehicleJSON, VehicleDTO.class);
            vehicleDTO = vehicleBean.create(vehicleDTO); 
            response =  Response.ok(vehicleDTO).build();
        } catch (JsonSyntaxException e) {
//            message.append("[Mensaje Syntax error gson]");
//            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("hola")
                    .build();
        } catch (JsonIOException e) {
//            message = new StringBuilder();
//            message.append("[Mensaje IO error gson]");
//            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("hola3")
                    .build();
        } catch (VehicleException e) {
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("hola2")
                    .build();
        }
        return response;
    }
}
