/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.clients.beans;

import com.enviosya.clients.dao.BaseDao;
import com.enviosya.clients.entities.ClientEntity;
import com.enviosya.clients.exceptions.ClientsException;
import com.sun.xml.internal.ws.util.StringUtils;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ws.rs.core.Response;

/**
 *
 * @author Marcos
 */
@Stateless
@LocalBean
public class ClientBean {
    
private static final String ENTIDAD_NULA = "La entidad es nula";

    @EJB
    private BaseDao<ClientEntity> clienteBD;
    public Response agregar(ClientEntity cliente) throws ClientsException
    {
        return null;
    }
      //  String ususarioLogeado = request.getHeader("usuario");
        //String token = request.getHeader("token");
    //    if (!esUsuarioAutenticado(ususarioLogeado, token)) {
    //        return Response.status(Response.Status.BAD_REQUEST).entity(USUARIO_NO_AUTENTICADO).build();
     //   }
 /*       if (cliente == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ENTIDAD_NULA).build();
        }
        if (hayCamposNulos(cliente)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(CAMPOS_OBLIGATORIOS_EN_BLANCO).build();
        }
        if (clienteBD.existeClientePorCi(cliente.getDocument())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(CI_CLIENTE_YA_EXISTE).build();
        }
        if (clienteBD.existeClientePorCorreoElectronico(clienteEntidad.getCorreoElectronico())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(CORREOELECTRONICO_CLIENTE_YA_EXISTE).build();
        }
        if (!esMailCorrecto(ClientEntity.getCorreoElectronico())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(CORREOELECTRONICO_FORMATO_INVALIDO).build();
        }
        consultaBd.agregar(ClientEntity);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(clienteEntidad)).build();
    }
    clienteBD.*/

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    /* private boolean hayCamposNulos(ClientEntity cliente) {
        return StringUtils.isBlank(cliente.getName()) || StringUtils.isBlank(cliente.getLastname())
                || StringUtils.isBlank(cliente.getDocument()) || StringUtils.isBlank(cliente.getEmail());
    }*/
}
