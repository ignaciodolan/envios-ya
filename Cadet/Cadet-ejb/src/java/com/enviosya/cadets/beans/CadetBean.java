/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.cadets.beans;

import com.enviosya.cadets.dao.BaseDAO;
import com.enviosya.cadets.dao.CadetDAO;
import com.enviosya.cadets.dao.VehicleDAO;
import com.enviosya.cadets.dto.CadetDTO;
import com.enviosya.cadets.entities.CadetEntity;
import com.enviosya.cadets.exceptions.CadetException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ws.rs.core.Response;

@Stateless
@LocalBean
public class CadetBean {
    
    
    
    private static final String NULL_ENTITY = " Entity to build is null";
    
    private static final String CADET_DOCUMENT_EXISTS
            = " The operation could not be completed because the document already exists";
    
    private static final String EMAIL_EXISTS
            = " The operation could not be completed because the email already exists";
    
    private static final String EMAIL_INVALID_FORMAT = " Email with invalid format";
    
    private static final String CADET_ID_DOES_NOT_EXISTS
            = " The operation could not be completed because there is no cadet with the given id";
    
    private static final String REQUIRED_BLANK_FIELDS
            = " The operation could not be completed because there are required fields empty";
    
    private static final String EMPTY_FIELDS_TO_ADD_VEHICLES
            = " No cadet was specified or no vehicles to be added";
    
    private static final String EMPTY_FIELDS_TO_REMOVE_VEHICLES = " No cadet or vehicle was specified";
    
    private static final String VEHICLES_DOES_NOT_EXISTS = " Some of the specified vehicles do not exist";
    
    private static final String VEHICLE_DOES_NOT_EXISTS = " The specified vehicle does not exist";
    
    private static final String CADET_WITH_SHIPMENTS
            = " The cadet can not be deleted because it's registered in a shipment";
    
    private static final String NO_SEARCH_RESULTS = " No results were found in the search";
    
    private static final String NOT_AUTHENTICATED_USER = " Invalid user or token";
    @EJB
    private CadetDAO cadetDAO;
//    @EJB 
//    private VehicleDAO vehicleDAO;
    @EJB
    private VehicleBean vehicleBean;

    public CadetDTO create(CadetDTO cadetDTO) throws CadetException {
        //TODO: check that the user is logged in
        if(cadetDTO == null){
            throw new CadetException(NULL_ENTITY);
        }
        if(nullValuesInCadetExist(cadetDTO)){
            throw new CadetException(REQUIRED_BLANK_FIELDS);
        }
        if(!isEmailValid(cadetDTO.getEmail())){
            throw new CadetException(EMAIL_INVALID_FORMAT);
        }
        if(documentExists(cadetDTO.getDocument())){
            throw new CadetException(CADET_DOCUMENT_EXISTS);
        }
        if(emailExists(cadetDTO.getEmail())){
            throw new CadetException(EMAIL_EXISTS);
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
        Matcher matcher = pattern.matcher(email); 
        return matcher.find() == true;
    }

    private boolean documentExists(String document) {
        return cadetDAO.documentExists(document);
    }
    
    private boolean emailExists(String email) {
        return cadetDAO.emailExists(email);
    }
    
    private boolean cadetIdExists(Long id) {
        return cadetDAO.idExists(id);
    }
    
    private boolean vehicleIdExists (Long id) {
        return vehicleBean.vehicleExists(id);
    }
     private boolean documentExists(String document, String ignoreDocument) {
        return cadetDAO.documentExists(document, ignoreDocument);
    }
    
    private boolean emailExists(String email, String ignoreEmail) {
        return cadetDAO.emailExists(email, ignoreEmail);
    }

    public void addVehicleToCadet(CadetDTO cadetDTO) throws CadetException {
        if (cadetDTO == null){
            throw new CadetException(NULL_ENTITY);
        }
        if (!cadetIdExists(cadetDTO.getId())) {
            throw new CadetException(CADET_ID_DOES_NOT_EXISTS);
        }
        if (cadetDTO.getVehiclesIds().isEmpty()) {
            throw new CadetException(VEHICLES_DOES_NOT_EXISTS);
        }
        if (!vehicleBean.vehiclesExists(cadetDTO.getVehiclesIds())) {
            throw new CadetException(VEHICLE_DOES_NOT_EXISTS);
        }
        cadetDAO.associateVehicles(cadetDTO);
        
    }
    public void removeVehicle(Long cadetId, Long vehicleId) throws CadetException {
        
        
        if (cadetId == null || vehicleId == null) {
            throw new CadetException(EMPTY_FIELDS_TO_REMOVE_VEHICLES);
        }
        if (!cadetIdExists(cadetId)) {
            throw new CadetException(CADET_ID_DOES_NOT_EXISTS);
        }
        if (!vehicleIdExists(vehicleId)) {
            throw new CadetException(VEHICLE_DOES_NOT_EXISTS);
        }
        cadetDAO.disassociate(cadetId, vehicleId);
        
    }
    
    public boolean vehiclesExists(List<Long> vehiclesId) {
        for (Long vehicleId : vehiclesId) {
            if (vehicleIdExists(vehicleId)) {
                return false;
            }
        }
        return true;
    }

    public List<CadetDTO> getCadets() throws CadetException {
        List<CadetDTO> cadets = cadetDAO.list();
        if(cadets.isEmpty()){
           throw new CadetException(NO_SEARCH_RESULTS);
        }
        return cadets;
    }

    public CadetDTO getCadet(Long id) throws CadetException {
        if (!cadetIdExists(id)) {
            throw new CadetException(CADET_ID_DOES_NOT_EXISTS);
        }
        return cadetDAO.get(id);
    }

    public CadetDTO modify(CadetDTO cadetDTO, Long id) throws CadetException {
        if (cadetDTO == null){
            throw new CadetException(NULL_ENTITY);
        }
        if(nullValuesInCadetExist(cadetDTO)){
            throw new CadetException(REQUIRED_BLANK_FIELDS);
        }
        if(!isEmailValid(cadetDTO.getEmail())){
            throw new CadetException(EMAIL_INVALID_FORMAT);
        }
        
        if (!cadetIdExists(id)) {
            throw new CadetException(CADET_ID_DOES_NOT_EXISTS);
        }
        
        CadetDTO oldCadetDTO = cadetDAO.modify(cadetDTO,id);
        
        if(documentExists(cadetDTO.getDocument(), oldCadetDTO.getDocument())){
            throw new CadetException(CADET_DOCUMENT_EXISTS);
        }
        if(emailExists(cadetDTO.getEmail(), oldCadetDTO.getEmail())){
            throw new CadetException(EMAIL_EXISTS);
        }
        return oldCadetDTO;
    }

    public void remove(Long id) throws CadetException {
        if (!cadetIdExists(id)){
            throw new CadetException(CADET_ID_DOES_NOT_EXISTS);
        }
        cadetDAO.removeCadet(id);
    }

    
}
