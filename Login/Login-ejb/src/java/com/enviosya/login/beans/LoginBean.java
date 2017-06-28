
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
        //Genero el token
        String hourMD5 = DigestUtils.md5Hex(calendar.getTime().toString());
        //Cambiar el getTime por el token de facebook
        String facebookTokenMD5 = DigestUtils.md5Hex(calendar.getTime().toString());
        StringBuilder token = new StringBuilder();
        token.append(hourMD5);
        token.append(facebookTokenMD5);
        //El userName de facebook
        String userName = "user_Name";
        LoginDTO loginDTO = new LoginDTO(userName, token.toString(), new Date(), tokenExpirationDate);
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

    
//    public Response agregarUsuario(String usuario, String clave, String esAdmin) {
//        if (StringUtils.isBlank(usuario) || StringUtils.isBlank(clave)) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(CAMPOS_VACIOS).build();
//        }
//
//        if (usuarioYaSeEncuentraRegistrado(usuario)) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(USUARIO_YA_REGISTRADO).build();
//        }
//        String claveEncriptadaConMD5 = DigestUtils.md5Hex(clave);
//        String esAdministrador = StringUtils.isBlank(esAdmin) ? "N" : esAdmin;
//        LoginUsuarioEntidad usuarioEntidad = new LoginUsuarioEntidad(usuario, claveEncriptadaConMD5, esAdministrador);
//        loginDAO.agregarUsuario(usuarioEntidad);
//        return Response.status(Response.Status.CREATED).entity(OPERACION_EXITOSA).build();
//    }
//
//    private boolean usuarioYaSeEncuentraRegistrado(String usuario) {
//        return loginDAO.validarUsuario(usuario);
//    }
//
//    
//    public void registrarAccesoNoAutorizado(String usuario, String clave) {
//        String claveEncriptadaConMD5 = DigestUtils.md5Hex(clave);
//        LoginAuditoriaEntidad loginEntidad = new LoginAuditoriaEntidad(usuario, claveEncriptadaConMD5, new Date(), "N");
//        loginDAO.agregarLoginNoAutenticado(loginEntidad);
//    }
//    
//    public void registrarAccesoAutorizado(String usuario, String clave) {
//        String claveEncriptadaConMD5 = DigestUtils.md5Hex(clave);
//        LoginAuditoriaEntidad loginEntidad = new LoginAuditoriaEntidad(usuario, claveEncriptadaConMD5, new Date(), "S");
//        loginDAO.agregarLoginAutenticado(loginEntidad);
//    }
//
//    public Response esAdministrador(String nombreUsuario) {
//        if (loginDAO.esAdministrador(nombreUsuario)) {
//            return Response.status(Response.Status.CREATED).entity("Es administrador").build();   
//        }
//        return Response.status(Response.Status.CREATED).entity("No es administrador").build();
//    }

}
