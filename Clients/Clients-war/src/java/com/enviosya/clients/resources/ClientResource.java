package com.enviosya.clients.resources;

import com.enviosya.clients.beans.ClientBean;
import com.enviosya.clients.entities.ClientEntity;
import com.enviosya.clients.exceptions.ClientsException;
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
    public Response agregar(String mensajeJson) { 
        StringBuilder mensaje;
        try {
             //  ClientEntity cliente = gson.fromJson(mensajeJson, ClientEntity.class);
          //      return clientBean.agregar(cliente);
        //       return null;
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
