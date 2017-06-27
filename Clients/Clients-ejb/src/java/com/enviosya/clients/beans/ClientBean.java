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
import java.util.ArrayList;
import java.util.List;
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
    
private static final String NULL_ENTITY = "The client entity is null";

private static final String NULL_VALUES = "The client has null values";

private static final String INVALID_EMAIL = "The client has invalid email";

private static final String EMAIL_ALREADY_EXISTS = "The email already exists";

private static final String DOCUMENT_ALREADY_EXISTS = "The document already exists";

private static final String CLIENT_NOT_EXISTS = "The client doesn't exist";

private static final String NO_RESULTS = "No results for that query";

    @EJB
    private ClientDao clientDAO;
    
    public ClientDTO create(ClientDTO clientDTO) throws ClientException{
        //TODO: check that the user is logged in
        if(clientDTO == null){
            throw new ClientException(NULL_ENTITY);
        }
        if(nullValuesInCadetExist(clientDTO)){
            throw new ClientException(NULL_VALUES);
        }
        if(!isEmailValid(clientDTO.getEmail())){
            throw new ClientException(INVALID_EMAIL);
        }
        if(documentExists(clientDTO.getDocument())){
            throw new ClientException(DOCUMENT_ALREADY_EXISTS);
        }
        if(emailExists(clientDTO.getEmail())){
            throw new ClientException(EMAIL_ALREADY_EXISTS);
        }
        
        Encryptor encryptor = new Encryptor();
        if (clientDTO.getCreditCardNumber() != null) {
            String encryptedCreditCardNumber = encryptor.encrypt(clientDTO.getCreditCardNumber());
            clientDTO.setCreditCardNumber(encryptedCreditCardNumber);
        }
        
        if (clientDTO.getCvc() != null) {
            String encryptedCvc = encryptor.encrypt(clientDTO.getCvc());
            clientDTO.setCvc(encryptedCvc);
        }
            
        clientDTO = clientDAO.create(clientDTO);
        
        return clientDTO;
    }
    
    public ClientDTO modify(ClientDTO clientDTO) throws ClientException{
        //chequear si el usuario esta logueado        
        if(clientDTO == null){
            throw new ClientException(NULL_ENTITY);
        }
        if(nullValuesInCadetExist(clientDTO)){
            throw new ClientException(NULL_VALUES);
        }
        if(!isEmailValid(clientDTO.getEmail())){
            throw new ClientException(INVALID_EMAIL);
        }
        //Esto esta mal arreglar como está en cadetes
        if(!documentExists(clientDTO.getDocument())){
            throw new ClientException(CLIENT_NOT_EXISTS);
        }
        Encryptor encryptor = new Encryptor();
        if (clientDTO.getCreditCardNumber() != null) {
            String encryptedCreditCardNumber = encryptor.encrypt(clientDTO.getCreditCardNumber());
            clientDTO.setCreditCardNumber(encryptedCreditCardNumber);
        }
        
        if (clientDTO.getCvc() != null) {
            String encryptedCvc = encryptor.encrypt(clientDTO.getCvc());
            clientDTO.setCvc(encryptedCvc);
        }
        clientDTO = clientDAO.modify(clientDTO);
        
        return clientDTO;
    }
    
    public void delete(ClientDTO clientDTO) throws ClientException{
        //chequear si el usuario esta logueado
        
        if(!documentExists(clientDTO.getDocument())){
            throw new ClientException(CLIENT_NOT_EXISTS);
        }       
        clientDAO.delete(clientDTO);
    }
    

    public List<ClientDTO> getClientList() throws Exception{
        // Chequear si esta logueado
        try {
        
            List<ClientEntity> clients = clientDAO.getClients();
            
            List<ClientDTO> clientsDTO = new ArrayList<ClientDTO>();

            clients.forEach((client) -> {
                clientsDTO.add(clientDAO.toDTO(client));
            });
            
            return clientsDTO;

        } catch (Exception e) {
            
            throw e;
        
        }
        
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
        return clientDAO.documentExists(document);
    }
    
    private boolean emailExists(String email) {
        return clientDAO.emailExists(email);
    }

    public ClientDTO getClientById(Long id) throws ClientException {
        //chequear si el usuario esta logueado
        if(id == null){
            throw new ClientException(NULL_ENTITY);
        }
        
        ClientEntity clientEntity = clientDAO.find(id);
        
        if(clientEntity == null){
            
            throw new ClientException(CLIENT_NOT_EXISTS);    
            
        }
        
        ClientDTO clienteDTO = clientDAO.toDTO(clientEntity);
        
        return clienteDTO;
    }
}
