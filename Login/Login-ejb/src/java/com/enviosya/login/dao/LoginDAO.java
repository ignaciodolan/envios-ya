
package com.enviosya.login.dao;

import com.enviosya.login.dto.LoginDTO;
import com.enviosya.login.entities.LoginEntity;
import com.enviosya.login.entities.LoginUserEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
            LoginEntity loginEntity = toLoginEntity(loginDTO);
            loginEntity = (LoginEntity) persist(loginEntity);
            return loginEntityToDTO(loginEntity);
        } catch (Exception e) {
            throw e;
        }  
    }
    
    public boolean activeUserToken(Date today, String token) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            StringBuilder query = new StringBuilder();
            query.append("select l from LoginUserEntity l where l.tokenExpirationDate > :today and l.token = :token");
            return !(entityManager.createQuery(query.toString())
                    .setParameter("token", token)
                    .setParameter("tokenExpirationDate", calendar.getTime(), TemporalType.DATE)
                    .getResultList().isEmpty());
        } catch (Exception e) {
            throw e;
        }
    }
        
    private LoginDTO loginEntityToDTO(LoginEntity entity) {
        LoginDTO loginDTO = new LoginDTO(entity.getUser(), null, entity.getLoginDate(), null);
        return loginDTO;
    }
    
//    private LoginDTO LoginUserEntityToDTO(LoginUserEntity entity) {
//        LoginDTO loginDTO = new LoginDTO (entity.getUserName(), entity.getToken(),
//            entity.getTokenCreationDate(),entity.getTokenExpirationDate());
//        return loginDTO;
//    }
    
    private LoginEntity toLoginEntity(LoginDTO loginDTO) {
        LoginEntity entity = new LoginEntity();
        entity.setUser(loginDTO.getUserName());
        entity.setLoginDate(loginDTO.getCreatedTokenDate());
        return entity;
    }
    
    private LoginUserEntity toLoginUserEntity(LoginDTO loginDTO) {
        LoginUserEntity entity = new LoginUserEntity();
        entity.setUserName(loginDTO.getUserName());
        entity.setToken(loginDTO.getToken());
        entity.setTokenCreationDate(loginDTO.getCreatedTokenDate());
        entity.setTokenExpirationDate(loginDTO.getLastConnectionDate());
        return entity;
    }

    public boolean isRegisterdUser(String userName) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("select l from LoginUserEntity l where l.userName > :userName");
            return !(entityManager.createQuery(query.toString())
                    .setParameter("userName", userName)
                    .getResultList().isEmpty());
        } catch (Exception e) {
            throw e;
        }
    
    }

    public void registerUser(LoginDTO user) {
        try {
            LoginUserEntity entity = toLoginUserEntity(user);
            entityManager.persist(entity);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
