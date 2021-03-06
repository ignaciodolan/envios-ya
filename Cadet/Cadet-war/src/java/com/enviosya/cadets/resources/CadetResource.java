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
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.enviosya.logger.LoggerEnviosYa;

/**
 * REST Web Service
 * 
 */
@Path("cadet")
public class CadetResource {

    @EJB
    private CadetBean cadetBean;
    
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(CadetResource.class);
    
    @Context
    private UriInfo context;
    
    private static final String SUCCESSFUL_OPERATION = " Operation completed successfully";

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
    public Response getCadets() {
        StringBuilder message;
        Response response;
        try {
            List<CadetDTO> cadets = cadetBean.getCadets();
            message = new StringBuilder();
            message.append("List of cadets:");
            message.append(gson.toJson(cadets));
            logger.success(message.toString());
            response = Response.ok().entity(gson.toJson(cadets)).build();
            
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        StringBuilder message;
        Response response;
        try {
            CadetDTO cadet = cadetBean.getCadet(id);
            message = new StringBuilder();
            message.append("Cadet:");
            message.append(gson.toJson(cadet));
            logger.success(message.toString());
            response = Response.ok().entity(gson.toJson(cadet)).build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        
        }
        return response;
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
            message.append("Cadet was created:");
            message.append(gson.toJson(cadetDTO));
            logger.success(message.toString());
            response = Response.status(Response.Status.CREATED).entity(gson.toJson(cadetDTO)).build();
        } catch (JsonSyntaxException e) {
            message = new StringBuilder();
            message.append("[Message Syntax error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Message IO error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        }
        return response;
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@PathParam("id") Long id, String messageJson) {
        StringBuilder message;
        Response response;
        try {
            CadetDTO newCadet = gson.fromJson(messageJson, CadetDTO.class);
            newCadet = cadetBean.modify(newCadet, id);
            message = new StringBuilder();
            message.append("Cadet was modified: ");
            message.append(gson.toJson(newCadet));
            logger.success(message.toString());
            response = Response.status(Response.Status.CREATED).entity(gson.toJson(newCadet)).build();
        } catch (JsonSyntaxException e) {
            message = new StringBuilder();
            message.append("[Message Syntax error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Message IO error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        }
        return response;
    }
    
    @POST
    @Path("/vehicles")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addVehicles(String mensajeJson) {
        StringBuilder message;
        Response response;
        try {
            CadetDTO cadetDTO = gson.fromJson(mensajeJson, CadetDTO.class);
            cadetBean.addVehicleToCadet(cadetDTO);
            message = new StringBuilder();
            message.append("Vehicle added to cadet: ");
            message.append(gson.toJson(cadetDTO));
            logger.success(message.toString());
            response = Response.status(Response.Status.CREATED).entity(gson.toJson(cadetDTO)).build();
        } catch (JsonSyntaxException e) {
            message = new StringBuilder();
            message.append("[Message Syntax error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.BAD_REQUEST).entity(message.toString()).build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Message IO error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        }
        return response;
  
    }
    @PUT
    @Path("/{cadetId}/removeVehicle/{vehicleId}")
    public Response removeVehicle(@PathParam("cadetId") Long cadetId, @PathParam("vehicleId") Long vehicleId) {
        StringBuilder message;
        Response response;
        try {
            cadetBean.removeVehicle(cadetId, vehicleId);
            message = new StringBuilder();
            message.append("Vehicle (id:").append(vehicleId).append(") was deleted from cadet (id:").append(cadetId).append(")");
            logger.success(message.toString());
            response = Response.status(Response.Status.OK).entity(SUCCESSFUL_OPERATION).build();
        } catch (JsonSyntaxException e) {
            message = new StringBuilder();
            message.append("[Message Syntax error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Message IO error gson]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]");
            message.append(e.getMessage());
            logger.error(message.toString());
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        }
        return response;
    }


    /**
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") Long id) {
        StringBuilder message;
        Response response;
        try {
            cadetBean.remove(id);
            message = new StringBuilder();
            message.append("Cadet (id:").append(id).append(") was deleted:");
            logger.success(message.toString());
            response = Response.status(Response.Status.OK).entity(SUCCESSFUL_OPERATION).build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }
    
    @GET
    @Path("/nearby/{latitude}/{length}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response nearbyCadets(@PathParam("latitude") Long latitude, @PathParam("length") Long length) {
        StringBuilder message;
        Response response;
        try {
            List<CadetDTO> cadets = cadetBean.getNearbyCadets(latitude, length);
            message = new StringBuilder();
            message.append("List of nerby cadets: ");
            message.append(gson.toJson(cadets));
            logger.success(message.toString());
            response = Response.ok().entity(gson.toJson(cadets)).build();
        } catch (CadetException e) {
            message = new StringBuilder();
            message.append("[Message Cadet Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }
}
