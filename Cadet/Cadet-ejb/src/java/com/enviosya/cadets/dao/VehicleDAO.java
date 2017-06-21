package com.enviosya.cadets.dao;

import com.enviosya.cadets.entities.VehicleEntity;
import com.enviosya.cadets.dto.VehicleDTO;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class VehicleDAO extends BaseDAO{

    @PersistenceContext
    private EntityManager entityManager;

    public VehicleDAO() {
        super(VehicleEntity.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public VehicleDTO create(VehicleDTO vehicleDTO) {
        VehicleEntity vehicleEntity = toEntity(vehicleDTO);
        vehicleEntity = (VehicleEntity) persist(vehicleEntity);
        return toDTO(vehicleEntity);
    }

    private VehicleEntity toEntity(VehicleDTO vehicleDTO) {
        VehicleEntity entity = new VehicleEntity();
        entity.setLicencePlate(vehicleDTO.getLicencePlate());
        entity.setDescription(vehicleDTO.getDescription());
       
        return entity;
    }
     private VehicleDTO toDTO(VehicleEntity entity) {
        VehicleDTO vehicleDTO = new VehicleDTO(entity.getId(), entity.getLicencePlate(),
                entity.getDescription());
        return vehicleDTO;
    }
     
     public boolean licencePlateExists(String licencePlate){
        return findByAttribute(licencePlate, "licencePlate") != null;
    }
    
}
