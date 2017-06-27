/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.login.resources;

import com.enviosya.logger.LoggerEnviosYa;
import com.enviosya.login.beans.LoginBean;
import com.enviosya.login.dto.LoginDTO;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("login")
public class LoginResource {
    
    
    @EJB
    private LoginBean loginBean;

    private final LoggerEnviosYa logger = new LoggerEnviosYa(LoginResource.class);
    
    @Context
    private UriInfo context;


    private final Gson gson = new Gson();

    public LoginResource() {
    }

    @GET
    @Path("/{id}/verifyToken/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyToken(@PathParam("id") Long id, @PathParam("token") String token) {
        StringBuilder message;
        Response response;
        try {
//            loginBean.verifyToken(token, id);
            message = new StringBuilder();
            message.append("Token verification:");
            message.append(gson.toJson(token));
            logger.success(message.toString());
            response = Response.ok().entity(gson.toJson("Eltoken")).build();
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doLogin(String messageJson) {
        StringBuilder message;
        Response response;
        LoginDTO loginDTO = gson.fromJson(messageJson, LoginDTO.class);
        try {
            
            //Llamar a la pagina de login
            //Recibir la data y mandarsela al bean
            response = Response.ok().entity(gson.toJson("Login success")).build();
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
        }
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String messageJson) {
        StringBuilder message;
        Response response;
        try {
            //Agarrar los datos de facebook y del json xa realizar el registro.
           //loginBean.register();
           response = Response.ok().entity(gson.toJson("Register success")).build();
        }
       catch (JsonSyntaxException e) {
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
        } catch (Exception e) {
            message = new StringBuilder();
            message.append("[Exception]: ");
            message.append(e.getMessage());
            logger.error(message.toString());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message.toString()).build();
        }
        return response;
    }
}
