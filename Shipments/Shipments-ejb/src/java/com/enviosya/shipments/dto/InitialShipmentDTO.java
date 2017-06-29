package com.enviosya.shipments.dto;

import java.util.List;

public class InitialShipmentDTO {
    private Long id;
    private String description;
    private Long clientSender;
    private Long clientReceiver;
    private String addressSender;
    private String addressReceiver;
    private List<Long> cadetList;
    private String packagePhoto;
    private Double comission;
    private Double cost;
    
    public InitialShipmentDTO(Long id, String description, Long clientSender, Long clientReceiver, String addressSender, String addressReceiver, Long cadet, String packagePhoto, Double cost, Double comission,  List<Long> cadetsIds) {
        this.id = id;
        this.description = description;
        this.clientSender = clientSender;
        this.clientReceiver = clientReceiver;
        this.addressSender = addressSender;        
        this.addressReceiver = addressReceiver;
        this.cadetList = cadetsIds;
        this.packagePhoto = packagePhoto;
        this.comission = comission;
        this.cost = cost;
    }

    public InitialShipmentDTO(ShipmentDTO shipmentDTO) {
        this.id = shipmentDTO.getId();
        this.description = shipmentDTO.getDescription();
        this.clientSender = shipmentDTO.getClientSender();
        this.clientReceiver = shipmentDTO.getClientReceiver();
        this.addressSender = shipmentDTO.getAddressSender();        
        this.addressReceiver = shipmentDTO.getAddressReceiver();
        this.packagePhoto = shipmentDTO.getPackagePhoto();
        this.comission = shipmentDTO.getComission();
        this.cost = shipmentDTO.getCost();
    }


    public String getPackagePhoto() {
        return packagePhoto;
    }

    public void setPackagePhoto(String packagePhoto) {
        this.packagePhoto = packagePhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getClientSender() {
        return clientSender;
    }

    public void setClientSender(Long clientSender) {
        this.clientSender = clientSender;
    }

    public Long getClientReceiver() {
        return clientReceiver;
    }

    public void setClientReceiver(Long clientReceiver) {
        this.clientReceiver = clientReceiver;
    }

    public String getAddressSender() {
        return addressSender;
    }

    public void setAddressSender(String addressSender) {
        this.addressSender = addressSender;
    }

    public String getAddressReceiver() {
        return addressReceiver;
    }

    public void setAddressReceiver(String addressReceiver) {
        this.addressReceiver = addressReceiver;
    }

    public List<Long> getCadetList() {
        return cadetList;
    }

    public void setCadetList(List<Long> cadetList) {
        this.cadetList = cadetList;
    }

    public Double getComission() {
        return comission;
    }

    public void setComission(Double comission) {
        this.comission = comission;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
    
}
