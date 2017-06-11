package com.enviosya.cadets.dto;

import java.util.List;

public class CadetDTO {
    private Long id;
    private String document;
    private String name;
    private String lastName;
    private String email;
    private List<VehicleDTO> vehicles;

    public CadetDTO(String document, String name, String lastName, String email, List<VehicleDTO> vehicles) {
        this.document = document;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.vehicles = vehicles;
    }

    public CadetDTO(Long id, String document, String name, String lastName, String email, List<VehicleDTO> vehicles) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.vehicles = vehicles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<VehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDTO> vehicles) {
        this.vehicles = vehicles;
    }
    
    
    
}
