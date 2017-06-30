
package com.enviosya.reviewread.dto;

public class VehicleDTO {
    private String licensePlate;
    private String descrption;

    public VehicleDTO() {
    }

    public VehicleDTO(String licensePlate, String descrption) {
        this.licensePlate = licensePlate;
        this.descrption = descrption;
    }
    

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }
    
}
