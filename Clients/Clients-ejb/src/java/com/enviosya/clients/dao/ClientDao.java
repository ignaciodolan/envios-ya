/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.clients.dao;

import com.enviosya.clients.dto.ClientDTO;
import com.enviosya.clients.entities.ClientEntity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class ClientDao extends BaseDao{
   @PersistenceContext(unitName = "Clients-ejbPU")
    private EntityManager em;


    public ClientDao() {
        super(ClientEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public boolean emailExists(String email){
        return findByAttribute(email, "email") != null;
    }
    
    public boolean documentExists(String document){
        return findByAttribute(document, "document") != null;
    }

    public ClientDTO create(ClientDTO clientDTO) {
        ClientEntity clientEntity = toEntity(clientDTO);
        clientEntity = (ClientEntity) persist(clientEntity);
        return toDTO(clientEntity);
    }

    private ClientEntity toEntity(ClientDTO clientDTO) {
        ClientEntity entity = new ClientEntity();
        entity.setDocument(clientDTO.getDocument());
        entity.setName(clientDTO.getName());
        entity.setEmail(clientDTO.getEmail());
        entity.setLastName(clientDTO.getLastName());
        return entity;
    }
    
     private ClientDTO toDTO(ClientEntity entity) {
        ClientDTO clientDTO;
        clientDTO = new ClientDTO(entity.getId(), entity.getDocument(),
        entity.getEmail(), entity.getLastName(), entity.getName());
        return clientDTO;
    }

    public ClientDTO modify(ClientDTO clientDTO) {
        ClientEntity client = toEntity(clientDTO);
        ClientEntity originarClient = em.find(ClientEntity.class, client.getId());
        originarClient.setName(client.getName());
        originarClient.setLastName(client.getLastName());
        originarClient.setDocument(client.getDocument());
        originarClient.setEmail(client.getEmail());
        client = em.merge(originarClient);
        return toDTO(client);
    }

    public void delete(ClientDTO clientDTO) {
        ClientEntity client = toEntity(clientDTO);
        ClientEntity originarClient = em.find(ClientEntity.class, client.getId());
        em.remove(originarClient);
    }
    
    
}
