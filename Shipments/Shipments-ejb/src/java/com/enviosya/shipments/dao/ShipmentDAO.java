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
    
    public ShipmentEntity get (Long shipmentId) {
        return (ShipmentEntity) find(shipmentId);
    }

    public boolean idExists(Long shipmentId) {
        return get(shipmentId) != null;
    }

    public ShipmentDTO create(ShipmentDTO shipmentDTO) {
        ShipmentEntity shipmentEntity = toEntity(shipmentDTO);
        shipmentEntity = (ShipmentEntity) persist(shipmentEntity);
        return toDTO(shipmentEntity);
    }

    public ShipmentEntity toEntity(ShipmentDTO shipmentDTO) {
        ShipmentEntity entity = new ShipmentEntity();
        entity.setDescription(shipmentDTO.getDescription());
        entity.setClientSender(shipmentDTO.getClientSender());
        entity.setClientReceiver(shipmentDTO.getClientReceiver());
        entity.setAddressReceiver(shipmentDTO.getAddressReceiver());
        entity.setAddressSender(shipmentDTO.getAddressSender());
        entity.setCadet(shipmentDTO.getCadet());
        entity.setPackagePhoto(shipmentDTO.getPackagePhoto());
        entity.setCost(shipmentDTO.getCost());
        entity.setComission(shipmentDTO.getComission());
        return entity;
    }

    public ShipmentDTO toDTO(ShipmentEntity entity) {
        ShipmentDTO shipmentDTO = new ShipmentDTO(entity.getId(), 
                entity.getDescription(), entity.getClientSender(), entity.getClientReceiver(),
                entity.getAddressSender(), entity.getAddressReceiver(), entity.getCadet(),
                entity.getPackagePhoto(),  entity.getCost(), entity.getComission(), entity.getStatus());
        return shipmentDTO;
    }

    public ShipmentDTO addCadetToShipment(Long shipmentId, Long cadetId) {
        ShipmentEntity shipment =  get(shipmentId);
        shipment.setCadet(cadetId);
        entityManager.merge(shipment);
        return this.toDTO(shipment);
    }

    public ShipmentDTO changeShipmentStatus(Long shipmentId, int status) {
        ShipmentEntity shipment =  get(shipmentId);
        shipment.setStatus(status);
        entityManager.merge(shipment);
        return this.toDTO(shipment);
    }

}
 