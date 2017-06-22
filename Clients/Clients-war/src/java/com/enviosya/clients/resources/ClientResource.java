package com.enviosya.clients.resources;

import com.enviosya.clients.beans.ClientBean;
import com.enviosya.clients.dto.ClientDTO;
import com.enviosya.clients.entities.ClientEntity;
import com.enviosya.clients.exceptions.ClientException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
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
    private final Gson gson = new Gson();
    
    public ClientResource() {
    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createClient(String ClientJSON){ 
         
        Response response;
        StringBuilder message = null;
        try {
            ClientDTO clienteDTO = gson.fromJson(ClientJSON, ClientDTO.class);
            clienteDTO = clientBean.create(clienteDTO); 
            message.append("Se creo exitosamente el cliente: ");
            message.append(clienteDTO);
            //TODO: Add log here
            //log.success(message);
            response =  Response.ok(clienteDTO).build();
        } catch (JsonSyntaxException e) {
            message.append("[Mensaje Syntax error gson]");
            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Mensaje IO error gson]");
            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        } catch (ClientException e) {
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        return response;
    }
   
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyClient(String ClientJSON){
        Response response;
        StringBuilder message = null;
        try {
            //primero usando gson transformo el stringJson en el objeto que necesito
            ClientDTO clienteDTO = gson.fromJson(ClientJSON, ClientDTO.class);
            clienteDTO = clientBean.modify(clienteDTO);
            message.append("Se modifico exitosamente el cliente: ");
            message.append(clienteDTO);
            //TODO: Add log here
            //log.success(message);
            response =  Response.ok(clienteDTO).build();
        }catch (JsonSyntaxException e) {
            message.append("[Mensaje Syntax error gson]");
            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(message.toString())
                    .build();
        } catch (JsonIOException e) {
            message = new StringBuilder();
            message.append("[Mensaje IO error gson]");
            message.append(e.getMessage());
            //TODO: Add log here
            //log.error(message);
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(message.toString())
                    .build();
        } catch (ClientException e) {
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        return response;
    }
/*
     @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        StringBuilder message;
        try {
            //return clientBean.getClientList();
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
    
*/
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
