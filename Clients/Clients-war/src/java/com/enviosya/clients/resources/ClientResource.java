package com.enviosya.clients.resources;

import com.enviosya.clients.beans.ClientBean;
import com.enviosya.clients.entities.ClientEntity;
import com.enviosya.clients.entities.PaymentMethodEntity;
import com.enviosya.clients.exceptions.ClientsException;
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


@Path("client")
public class ClientResource {

    @Context
    private UriInfo context;

    @EJB
    private ClientBean clientBean;
    //private final Gson gson = new Gson();
    
    public ClientResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addClient(String stringJson) { 
        StringBuilder message;
        try {
               ClientEntity clientEntity = null;//gson.fromJson(stringJson, ClientEntity.class);
                return clientBean.add(clientEntity);
        } catch (Exception e) {
        }
        
             
       /* } catch (JsonSyntaxException e) {
            mensaje = new StringBuilder();
            mensaje.append("Se produjo un error de sintáxis en el gson al intentar agregar un cliente. Excepción: ");
            mensaje.append(e.getMessage());
            //log.error(mensaje);
            return Response.status(Response.Status.BAD_REQUEST).entity(mensaje.toString()).build();
        }catch (JsonIOException e) {
            mensaje = new StringBuilder();
            mensaje.append("Se produjo un error de IO en el gson al intentar agregar un cliente. Excepción: ");
            mensaje.append(e.getMessage());
           // log.error(mensaje);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(mensaje.toString()).build();
        } catch (ClientsException e) {
            mensaje = new StringBuilder();
            mensaje.append("Exception: ");
            mensaje.append(e.getMessage());
            //log.error(mensaje);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(mensaje.toString()).build();
        }*/
       return null;
}
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyClient(String stringJson){
        StringBuilder message;
        try {
            //primero usando gson transformo el stringJson en el objeto que necesito
            ClientEntity modifyClient = null;//gson.fromJson(stringJson, ClientEntity.class);
            return clientBean.modify(modifyClient);
        } catch (Exception e) {
             message = new StringBuilder();
             message.append("Error a crear si se da una excepcion");
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
    }
    
    
    
     @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        StringBuilder message;
        try {
            return clientBean.getClientList();
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("Exception: ");
            message.append(e.getMessage());
//          //  log.error(mensaje);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientById(@PathParam("id") Long id) {
        StringBuilder message;
        try {
            return clientBean.searchById(id);
        } catch (Exception e) {
            message = new StringBuilder();
//            message.append("Exception: ");
//            message.append(e.getMessage());
//            //log.error(mensaje);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
    }
    
    @POST
    @Path("idClient/associatePaymentMethod/{idPaymentMethod}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response associatePaymentMethod(@PathParam("idClient") Long idClient, @PathParam("idClient") Long idPaymentMethod) {
        StringBuilder message;
        try {
            return clientBean.associatePaymentMethod(idClient, idPaymentMethod);
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("Exception: ");
            message.append(e.getMessage());
            //log.error(mensaje);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
    }
    
    @POST
    @Path("medioDePago")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPaymentMethod(String stringJson) {
        StringBuilder message;
        try {
            PaymentMethodEntity paymentMethodEntity = null;//gson.fromJson(stringJson, MedioPagoEntidad.class);
            return clientBean.addPaymentMethod(paymentMethodEntity);
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("Se produjo un error de sintáxis en el gson al intentar agregar un cliente. Excepción: ");
            message.append(e.getMessage());
//            log.error(mensaje);
            return Response.status(Response.Status.BAD_REQUEST).entity(message.toString()).build();
        }
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
