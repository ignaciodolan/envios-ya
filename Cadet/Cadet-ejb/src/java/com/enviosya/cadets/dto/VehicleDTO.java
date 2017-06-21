package com.enviosya.cadets.dto;

public class VehicleDTO {
    private long id;
    private String licencePlate;
    private String description;

    public VehicleDTO(long id, String licencePlate, String description) {
        this.id = id;
        this.licencePlate = licencePlate;
        this.description = description;
    }

    public VehicleDTO(String licencePlate, String description) {
        this.licencePlate = licencePlate;
        this.description = description;
    }

    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
