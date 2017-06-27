

package com.enviosya.login.beans;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;


@Stateless
@LocalBean
public class LoginBean {

    public void verifyToken(String token, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
