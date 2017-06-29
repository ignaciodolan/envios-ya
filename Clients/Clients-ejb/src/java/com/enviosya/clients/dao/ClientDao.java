/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.clients.dao;

import com.enviosya.clients.dto.ClientDTO;
import com.enviosya.clients.entities.ClientEntity;
import java.util.HashSet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ClientDao extends BaseDao{
   @PersistenceContext(unitName = "Clients-ejbPU")
    private EntityManager entityManager;


    public ClientDao() {
        super(ClientEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public boolean emailExists(String email){
        return !findByAttribute(email, "email").isEmpty();
    }
    
    public boolean documentExists(String document){
        return !findByAttribute(document, "document").isEmpty();
    }

    public ClientDTO create(ClientDTO clientDTO) {
        ClientEntity clientEntity = toEntity(clientDTO);
        clientEntity = (ClientEntity) persist(clientEntity);
        return toDTO(clientEntity);
    }

    public ClientEntity toEntity(ClientDTO clientDTO) {
        ClientEntity entity = new ClientEntity();
        entity.setDocument(clientDTO.getDocument());
        entity.setName(clientDTO.getName());
        entity.setLastName(clientDTO.getLastName());
        entity.setPaymentMethod(clientDTO.getPaymentMethod());
        entity.setEmail(clientDTO.getEmail());
        entity.setCreditCardNumber(clientDTO.getCreditCardNumber());
        entity.setCvc(clientDTO.getCvc());
        
        return entity;
    }
    
    public ClientDTO toDTO(ClientEntity entity) {
        ClientDTO clientDTO;
        clientDTO = new ClientDTO(entity.getId(), entity.getDocument(), entity.getName(), entity.getLastName(), 
        entity.getPaymentMethod(), entity.getEmail(), entity.getCreditCardNumber(), entity.getCvc());
        return clientDTO;
    }

    public ClientDTO modify(ClientDTO clientDTO) {
        ClientEntity originalClient = find(clientDTO.getId());
        originalClient.setName(clientDTO.getName());
        originalClient.setLastName(clientDTO.getLastName());
        originalClient.setDocument(clientDTO.getDocument());
        originalClient.setEmail(clientDTO.getEmail());
        originalClient.setPaymentMethod(clientDTO.getPaymentMethod());
        originalClient.setCreditCardNumber(clientDTO.getCreditCardNumber());
        originalClient.setCvc(clientDTO.getCvc());
        originalClient = entityManager.merge(originalClient);
        return toDTO(originalClient);
    }
    
    public ClientEntity find(Long id){
        return (ClientEntity) entityManager.find(ClientEntity.class, id);
    }
    
    public void delete(ClientDTO clientDTO) {
        ClientEntity client = toEntity(clientDTO);
        ClientEntity originalClient = entityManager.find(ClientEntity.class, client.getId());
        entityManager.remove(originalClient);
    }

    public List<ClientEntity> getClients() {
        try {
            List<ClientEntity> clients = (List<ClientEntity>) this.findAll();
            return clients;
        } catch (Exception e) {
            throw e;
        }
    }
    
    
}
