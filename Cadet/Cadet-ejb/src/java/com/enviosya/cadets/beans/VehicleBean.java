/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.cadets.beans;

import com.enviosya.cadets.dao.VehicleDAO;
import com.enviosya.cadets.dto.VehicleDTO;
import com.enviosya.cadets.exceptions.VehicleException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

@Stateless
@LocalBean
public class VehicleBean {
    @EJB
    private VehicleDAO vehicleDAO;

    public VehicleDTO create(VehicleDTO vehicleDTO) throws VehicleException {
        //TODO: check that the user is logged in
        if(vehicleDTO == null){
            throw new VehicleException("Invalid vehicle.");
        }
        if(nullValuesInVehicleExist(vehicleDTO)){
            throw new VehicleException("Missing required fields.");
        }
        if(licencePlateExists(vehicleDTO.getLicencePlate())){
            throw new VehicleException("Licence plate already in use.");
        }
        
        vehicleDTO = vehicleDAO.create(vehicleDTO);
        return vehicleDTO;
    }
    private boolean nullValuesInVehicleExist(VehicleDTO vehicleDTO) {
        return vehicleDTO.getLicencePlate() == null || vehicleDTO.getLicencePlate().isEmpty();
    }
    
    private boolean licencePlateExists(String licencePlate) {
        return vehicleDAO.licencePlateExists(licencePlate);
    }

}
