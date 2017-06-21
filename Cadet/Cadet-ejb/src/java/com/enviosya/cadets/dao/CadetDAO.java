package com.enviosya.cadets.dao;

import com.enviosya.cadets.dto.CadetDTO;
import com.enviosya.cadets.entities.CadetEntity;
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
    
    public boolean emailExists(String email){
        return findByAttribute(email, "email") != null;
    }
    
    public boolean documentExists(String document){
        return findByAttribute(document, "document") != null;
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
                entity.getEmail(), entity.getLastName(), entity.getName());
        return cadetDTO;
    }
}
