/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.clients.beans;

import com.enviosya.clients.dao.BaseDao;
import com.enviosya.clients.dao.ClientDao;
import com.enviosya.clients.dto.ClientDTO;
import com.enviosya.clients.entities.ClientEntity;
import com.enviosya.clients.exceptions.ClientException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private ClientDao clienteDao;
    
    public ClientDTO create(ClientDTO clientDTO) throws ClientException{
        //TODO: check that the user is logged in
        if(clientDTO == null){
            throw new ClientException("Invalid cadet.");
        }
        if(nullValuesInCadetExist(clientDTO)){
            throw new ClientException("Missing required fields.");
        }
        if(!isEmailValid(clientDTO.getEmail())){
            throw new ClientException("Invalid email");
        }
        if(documentExists(clientDTO.getDocument())){
            throw new ClientException("Document already in use.");
        }
        if(emailExists(clientDTO.getEmail())){
            throw new ClientException("Email already in use.");
        }
        
        clientDTO = clienteDao.create(clientDTO);
        return clientDTO;
    }
    
    public ClientDTO modify(ClientDTO clientDTO) throws ClientException{
        //chequear si el usuario esta logueado
        if(clientDTO == null){
            throw new ClientException("Invalid cadet.");
        }
        if(nullValuesInCadetExist(clientDTO)){
            throw new ClientException("Missing required fields.");
        }
        if(!isEmailValid(clientDTO.getEmail())){
            throw new ClientException("Invalid email");
        }
        if(!documentExists(clientDTO.getDocument())){
            throw new ClientException("You have to modify an existing client.");
        }
        clientDTO = clienteDao.modify(clientDTO);
        return clientDTO;
    }
    
    public void delete(ClientDTO clientDTO) throws ClientException{
        //chequear si el usuario esta logueado
        
        if(!documentExists(clientDTO.getDocument())){
            throw new ClientException("You have to delete an existing client.");
        }       
        clienteDao.delete(clientDTO);
    }
    
    public Response associatePaymentMethod(Long idClient, Long idPaymentMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public Response searchById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Response getClientList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

     private boolean nullValuesInCadetExist(ClientDTO clientDTO) {
        return clientDTO.getName() == null || clientDTO.getName().isEmpty() || clientDTO.getLastName() == null
                || clientDTO.getLastName().isEmpty() || clientDTO.getDocument() == null || clientDTO.getDocument().isEmpty()
                || clientDTO.getEmail() == null || clientDTO.getEmail().isEmpty();
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); 
        Matcher matcher = pattern.matcher(email); 
        return matcher.find() == true;
    }

    private boolean documentExists(String document) {
        return clienteDao.documentExists(document);
    }
    
    private boolean emailExists(String email) {
        return clienteDao.emailExists(email);
    }

}
