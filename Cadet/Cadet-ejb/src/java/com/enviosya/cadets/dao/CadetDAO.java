package com.enviosya.cadets.dao;

import com.enviosya.cadets.dto.CadetDTO;
import com.enviosya.cadets.entities.CadetEntity;
import com.enviosya.cadets.entities.VehicleEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CadetDAO extends BaseDAO{
    
    @PersistenceContext
    private EntityManager entityManager;

    public CadetDAO() {
        super(CadetEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    
    public CadetEntity get (Long cadetId) {
        return (CadetEntity) find(cadetId);
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

    public CadetDTO create(CadetDTO cadetDTO) {
        CadetEntity cadetEntity = toEntity(cadetDTO);
        cadetEntity = (CadetEntity) persist(cadetEntity);
        return toDTO(cadetEntity);
    }

    private CadetEntity toEntity(CadetDTO cadetDTO) {
        CadetEntity entity = new CadetEntity();
        entity.setDocument(cadetDTO.getDocument());
        entity.setName(cadetDTO.getName());
        entity.setEmail(cadetDTO.getEmail());
        entity.setLastName(cadetDTO.getLastName());
        return entity;
    }
    
     private CadetDTO toDTO(CadetEntity entity) {
        CadetDTO cadetDTO = new CadetDTO(entity.getId(), entity.getDocument(),
                entity.getName(),entity.getLastName(),entity.getEmail());
        return cadetDTO;
    }
     
    public void associateVehicles(CadetDTO cadetDTO) {
      try {
          Long cadetId = cadetDTO.getId();
          CadetEntity cadet = get(cadetId);
          for (Long id : cadetDTO.getVehiclesIds()) {
              VehicleEntity vehicle = entityManager.find(VehicleEntity.class, id);
              if (!cadet.getVehicles().contains(vehicle)) {
                  cadet.getVehicles().add(vehicle);
              }
          }
          entityManager.merge(cadet);
      } catch (Exception e) {
          throw e;
      }
    }

     
    
}
