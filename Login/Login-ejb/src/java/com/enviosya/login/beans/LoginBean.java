
package com.enviosya.login.beans;
import com.enviosya.login.dao.LoginDAO;
import com.enviosya.login.dto.LoginDTO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import com.enviosya.login.entities.LoginEntity;
import com.enviosya.login.exceptions.LoginException;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;


@Stateless
@LocalBean
public class LoginBean {
    
    @EJB
    private LoginDAO loginDAO;
    @Context 
    
    private HttpServletRequest request;

    private static final String EMPTY_TYPE = "Login type is empty";
    private static final String EMPTY_TOKEN = "Token is empty";
    private static final String INCORRECT_DATA = "Incorrect token";


    public LoginDTO doLogin(String loginType) throws LoginException{
        //Agregar al logger que el usuario se logueo
        if (StringUtils.isBlank(loginType)) {
            throw new LoginException(EMPTY_TYPE);
        }
 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //Sumo 15 dias a la fecha actual para que expire el token
        calendar.add(Calendar.DATE, 15);
        Date tokenExpirationDate  = calendar.getTime();
        //Llamar a la api de facebook
        //el userName de facebook
        String userName = "user_Name";
        //Genero el token
        String hourMD5 = DigestUtils.md5Hex(calendar.getTime().toString());
        //Cambiar el getTime por el token de facebook
        String facebookToken = "abcd1234";
        String facebookTokenMD5 = DigestUtils.md5Hex(facebookToken);
        StringBuilder token = new StringBuilder();
        token.append(hourMD5);
        token.append(facebookTokenMD5);
        LoginDTO loginDTO = new LoginDTO(userName, token.toString(), new Date(), tokenExpirationDate);
        if (!loginDAO.isRegisterdUser(userName)) {
            //Agregar en el logger que se registro el usuario
            loginDAO.registerUser(loginDTO);
        }
        //Agregar en el logger que el usuario inicio sesion
        return loginDAO.addLogin(loginDTO);
        
    }

    public boolean verifyToken(String token) throws LoginException{
        //agregar logger 
        if (StringUtils.isBlank(token)) {
            throw new LoginException(EMPTY_TOKEN);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return loginDAO.activeUserToken(calendar.getTime(), token);
    }    
}
