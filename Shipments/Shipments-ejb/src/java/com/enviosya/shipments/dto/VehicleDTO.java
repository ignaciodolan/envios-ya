package com.enviosya.shipments.dto;

public class VehicleDTO {
    private long id;
    private String licensePlate;
    private String description;

    public VehicleDTO(long id, String licensePlate, String description) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.description = description;
    }

    public VehicleDTO(String licensePlate, String description) {
        this.licensePlate = licensePlate;
        this.description = description;
    }

    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = this.licensePlate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
