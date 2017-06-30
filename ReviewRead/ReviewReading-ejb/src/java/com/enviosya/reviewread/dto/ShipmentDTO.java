package com.enviosya.reviewread.dto;

public class ShipmentDTO {
    private Long id;
    private String description;
    private Long clientSender;
    private Long clientReceiver;
    private String addressSender;
    private String addressReceiver;
    private Long cadet;
    private String packagePhoto;
    private Double comission;
    private Double cost;
    private int status;
    
    public ShipmentDTO(Long id, String description, Long clientSender,
            Long clientReceiver, String addressSender, String addressReceiver,
            Long cadet, String packagePhoto, Double cost, Double comission, int status) {
        this.id = id;
        this.description = description;
        this.clientSender = clientSender;
        this.clientReceiver = clientReceiver;
        this.addressSender = addressSender;        
        this.addressReceiver = addressReceiver;
        this.cadet = cadet;
        this.packagePhoto = packagePhoto;
        this.comission = comission;
        this.cost = cost;
        this.status = status;
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

    public Long getCadet() {
        return cadet;
    }

    public void setCadet(Long cadet) {
        this.cadet = cadet;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
