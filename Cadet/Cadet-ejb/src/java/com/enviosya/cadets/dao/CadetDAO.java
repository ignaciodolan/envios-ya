package com.enviosya.cadets.dao;

import com.enviosya.cadets.dto.CadetDTO;
import com.enviosya.cadets.entities.CadetEntity;
import com.enviosya.cadets.entities.VehicleEntity;
import java.util.ArrayList;
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
    
    
    public CadetDTO get (Long cadetId) {
        CadetEntity cadetEntity = (CadetEntity) find(cadetId);
       if (cadetEntity == null) {
       return null;
       } else {
        return toDTO(cadetEntity);
       }
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
          CadetEntity cadetEntity = toEntity(cadetDTO);
          for (Long id : cadetDTO.getVehiclesIds()) {
              VehicleEntity vehicle = entityManager.find(VehicleEntity.class, id);
              if (!cadetEntity.getVehicles().contains(vehicle)) {
                  cadetEntity.getVehicles().add(vehicle);
              }
          }
          entityManager.merge(cadetEntity);
      } catch (Exception e) {
          throw e;
      }
    }
    
    public List<CadetDTO> list () {
        List<CadetEntity> cadets;
        List<CadetDTO> cadetsDTO = new ArrayList<>();
      try {
          cadets = findAll();
          for (CadetEntity cadet: cadets) {
            cadetsDTO.add(toDTO(cadet));
        }
      } catch (Exception e) {
          throw e;
      }
        
        return cadetsDTO;
    }

    public CadetDTO modify(CadetDTO cadetDTO, Long id) {
        CadetEntity cadetEntity;
        CadetDTO oldCadet = get(id);
        if (oldCadet == null) {
            return null;
        }
        try {
            cadetEntity = (CadetEntity) find(id);
            cadetEntity.setName(cadetDTO.getName());
            cadetEntity.setLastName(cadetDTO.getLastName());
            cadetEntity.setDocument(cadetDTO.getDocument());
            cadetEntity.setEmail(cadetDTO.getEmail());
            edit(cadetEntity);
        } catch (Exception e) {
            throw e;
        }
        
        return toDTO(cadetEntity);
    }

    public boolean documentExists(String document, String ignoreDocument) {
        if (document.equals(ignoreDocument)) {
            return false;
        } else {
            return documentExists(document);
        }   
    }

    public boolean emailExists(String email, String ignoreEmail) {
        if (email.equals(ignoreEmail)) {
            return false;
        }
        else {
            return emailExists(email);
        }
    }
    
    public void removeCadet (Long id) {
        CadetEntity cadet = (CadetEntity) find(id);
        remove (cadet);
    }

    public void disassociate(Long cadetId, Long vehicleId) {
        try {
            CadetEntity cadet = (CadetEntity) find(cadetId);
            VehicleEntity vehicleToRemove = entityManager.find(VehicleEntity.class, vehicleId);
            cadet.getVehicles().remove(vehicleToRemove);
            edit(cadet);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public List<CadetDTO> getNearbyCadets(Long latitude, Long length) {
        List<CadetEntity> cadets = entityManager.createQuery("select c from CadetEntity c").setMaxResults(4).getResultList();
        List<CadetDTO> cadetsDTO = new ArrayList<>();
        for (CadetEntity cadet: cadets) {
            cadetsDTO.add(toDTO(cadet));
        }
        return cadetsDTO;
    }
    

     
}
