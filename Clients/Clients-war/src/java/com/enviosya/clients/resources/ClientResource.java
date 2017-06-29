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
import com.enviosya.logger.LoggerEnviosYa;
import java.util.List;


@Path("client")
public class ClientResource {

    @Context
    private UriInfo context;

    @EJB
    private ClientBean clientBean;
    private final Gson gson = new Gson();
    private final LoggerEnviosYa logger = new LoggerEnviosYa(ClientResource.class);
    
    public ClientResource() {
    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createClient(String ClientJSON){ 
        Response response;
        StringBuilder message;
        try {
            ClientDTO clienteDTO = gson.fromJson(ClientJSON, ClientDTO.class);
            clienteDTO = clientBean.create(clienteDTO); 
            message = new StringBuilder();
            message.append("Client was created: ");
            message.append(gson.toJson(clienteDTO));
            logger.success(message.toString());
            response =  Response.ok(clienteDTO).build();
            response = Response.status(Response.Status.CREATED).entity(gson.toJson(clienteDTO)).build();
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
        } catch (ClientException e) {
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        return response;
    }
   
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyClient(String ClientJSON){
        Response response;
        StringBuilder message;
        try {
            ClientDTO clientDTO = gson.fromJson(ClientJSON, ClientDTO.class);
            clientDTO = clientBean.modify(clientDTO);
            message = new StringBuilder();
            message.append("Client was modified: ");
            message.append(gson.toJson(clientDTO));
            logger.success(message.toString());
            response = Response.status(Response.Status.OK).entity(gson.toJson(clientDTO)).build();
        }catch (JsonSyntaxException e) {
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
        } catch (ClientException e) {
            response =  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
        
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        StringBuilder message;
        Response response;
        try {
            List<ClientDTO> clients = clientBean.getClientList();
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
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientById(@PathParam("id") Long id) {
        StringBuilder message;
        Response response;
        try {
            ClientDTO clientDTO = clientBean.getClientById(id);
            message = new StringBuilder();
            message.append("Client was modified: ");
            message.append(gson.toJson(clientDTO));
            logger.success(message.toString());
            response = Response.status(Response.Status.OK).entity(gson.toJson(clientDTO)).build();
        } catch (ClientException e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
