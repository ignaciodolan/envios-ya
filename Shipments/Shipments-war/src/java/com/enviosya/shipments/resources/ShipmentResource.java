package com.enviosya.shipments.resources;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.shipments.beans.ShipmentBean;
import com.enviosya.shipments.dto.InitialShipmentDTO;
import com.enviosya.shipments.dto.ShipmentDTO;
import com.enviosya.shipments.exceptions.ShipmentException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

@Path("shipment")
public class ShipmentResource {

    @Context
    private UriInfo context;
    
    @EJB
    private ShipmentBean shipmentBean;
    
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ShipmentResource.class);
    /**
     * Creates a new instance of ShipmentResource
     */
    public ShipmentResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createShipment(String shipmentJSON){ 
        Response response;
        StringBuilder message;
        try {
            ShipmentDTO shipmentDTO = gson.fromJson(shipmentJSON, ShipmentDTO.class);
            InitialShipmentDTO initialShipment = null;
            initialShipment = shipmentBean.create(shipmentDTO); 
            message = new StringBuilder();
            message.append("Initial shipment was created: ");
            message.append(gson.toJson(initialShipment));
            logger.success(message.toString());
            response = Response.status(Response.Status.CREATED).entity(gson.toJson(initialShipment)).build();
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
        } catch (ShipmentException e) {
            message = new StringBuilder();
            message.append("[ShipmentException]: ");
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
     * Retrieves representation of an instance of com.enviosya.shipments.ws.ShipmentResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ShipmentResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
