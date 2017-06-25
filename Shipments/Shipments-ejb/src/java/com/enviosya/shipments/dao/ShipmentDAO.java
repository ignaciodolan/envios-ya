package com.enviosya.shipments.dao;

import com.enviosya.shipments.dto.ShipmentDTO;
import com.enviosya.shipments.entities.ShipmentEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ShipmentDAO extends BaseDAO{
    
    @PersistenceContext
    private EntityManager entityManager;

    public ShipmentDAO() {
        super(ShipmentEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public ShipmentEntity get (Long cadetId) {
        return (ShipmentEntity) find(cadetId);
    }

    public boolean idExists(Long cadetId) {
        return get(cadetId) != null;
    }

    public boolean emailExists(String email){
        return !findByAttribute(email, "email").isEmpty();
    }
    
    public boolean documentExists(String document){
        return  !findByAttribute(document, "document").isEmpty();
    }

    public ShipmentDTO create(ShipmentDTO shipmentDTO) {
        ShipmentEntity cadetEntity = toEntity(shipmentDTO);
        cadetEntity = (ShipmentEntity) persist(cadetEntity);
        return toDTO(cadetEntity);
    }

    private ShipmentEntity toEntity(ShipmentDTO shipmentDTO) {
        ShipmentEntity entity = new ShipmentEntity();
        entity.setDocument(shipmentDTO.getDocument());
        entity.setName(shipmentDTO.getName());
        entity.setEmail(shipmentDTO.getEmail());
        entity.setLastName(shipmentDTO.getLastName());
        return entity;
    }

     private ShipmentDTO toDTO(ShipmentEntity entity) {
        ShipmentDTO shipmentDTO = new ShipmentDTO(entity.getId(), entity.getDocument(),
                entity.getName(),entity.getLastName(),entity.getEmail());
        return shipmentDTO;
    }

    public boolean idExists(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
