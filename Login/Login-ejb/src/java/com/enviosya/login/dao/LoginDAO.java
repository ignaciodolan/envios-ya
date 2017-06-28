
package com.enviosya.login.dao;

import com.enviosya.login.dto.LoginDTO;
import com.enviosya.login.entities.LoginEntity;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;


@Stateless
public class LoginDAO extends BaseDAO {
    
    @PersistenceContext
    private EntityManager entityManager;

    public LoginDAO() {
        super(LoginEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public LoginDTO addLogin(LoginDTO loginDTO) {
        try {
            LoginEntity loginEntity = toEntity(loginDTO);
            loginEntity = (LoginEntity) persist(loginEntity);
            return toDTO(loginEntity);
        } catch (Exception e) {
            throw e;
        }  
    }
    
    public boolean activeUserToken(Date today, String token) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            StringBuilder query = new StringBuilder();
            query.append("select l from LoginEntity l where l.tokenExpirationDate > :today and l.token = :token");
            return !(entityManager.createQuery(query.toString())
                    .setParameter("token", token)
                    .setParameter("tokenExpirationDate", calendar.getTime(), TemporalType.DATE)
                    .getResultList().isEmpty());
        } catch (Exception e) {
            throw e;
        }
    }
        
    private LoginDTO toDTO(LoginEntity entity) {
        LoginDTO loginDTO = new LoginDTO(entity.getUser(), entity.getToken(), 
                entity.getStartDate(),entity.getEndDate());
        return loginDTO;
    }
    
    private LoginEntity toEntity(LoginDTO loginDTO) {
        LoginEntity entity = new LoginEntity();
        entity.setUser(loginDTO.getUserName());
        entity.setToken(loginDTO.getToken());
        entity.setStartDate(loginDTO.getCreatedTokenDate());
        entity.setEndDate(loginDTO.getLastConnectionDate());
        return entity;
    }
    
}
