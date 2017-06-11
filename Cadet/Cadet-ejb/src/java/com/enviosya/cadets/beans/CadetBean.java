/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.cadets.beans;

import com.enviosya.cadets.dao.BaseDao;
import com.enviosya.cadets.dao.CadetDao;
import com.enviosya.cadets.dto.CadetDTO;
import com.enviosya.cadets.entities.CadetEntity;
import com.enviosya.cadets.exceptions.CadetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Ruso
 */
@Stateless
@LocalBean
public class CadetBean {
    
    @EJB
    private CadetDao cadetDAO;

    public CadetDTO create(CadetDTO cadetDTO) throws CadetException{
        //TODO: check that the user is logged in
        if(cadetDTO == null){
            throw new CadetException("Invalid cadet.");
        }
        if(nullValuesInCadetExist(cadetDTO)){
            throw new CadetException("Missing required fields.");
        }
        if(!isEmailValid(cadetDTO.getEmail())){
            throw new CadetException("Invalid email");
        }
        if(documentExists(cadetDTO.getDocument())){
            throw new CadetException("Document already in use.");
        }
        if(emailExists(cadetDTO.getEmail())){
            throw new CadetException("Email already in use.");
        }
        
        cadetDTO = cadetDAO.create(cadetDTO);
        return cadetDTO;
    }

    private boolean nullValuesInCadetExist(CadetDTO cadetDTO) {
        return cadetDTO.getName() == null || cadetDTO.getName().isEmpty() || cadetDTO.getLastName() == null
                || cadetDTO.getLastName().isEmpty() || cadetDTO.getDocument() == null || cadetDTO.getDocument().isEmpty()
                || cadetDTO.getEmail() == null || cadetDTO.getEmail().isEmpty();
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); 
        Matcher mather = pattern.matcher(email); 
        return mather.find() == true;
    }

    private boolean documentExists(String document) {
        return cadetDAO.documentExists(document);
    }
    
    private boolean emailExists(String email) {
        return cadetDAO.emailExists(email);
    }

    
}
