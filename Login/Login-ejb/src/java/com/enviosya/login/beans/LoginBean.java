
package com.enviosya.login.beans;
import com.enviosya.login.dao.LoginDAO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;


@Stateless
@LocalBean
public class LoginBean {
    
    @EJB
    private LoginDAO loginDAO;
    
    public void verifyToken(String token, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
