package com.enviosya.shipments.dto;

public class ShipmentDTO {
    private Long id;
    private String description;
    private Long clientSender;
    private Long clientReceiver;
    private String addressSender;
    private String addressReceiver;
    private Long cadet;
    private String packagePhoto;
    public ShipmentDTO(Long id, String description, Long clientSender, Long clientReceiver, String addressSender, String addressReceiver, Long cadet) {
        this.id = id;
        this.description = description;
        this.clientSender = clientSender;
        this.clientReceiver = clientReceiver;
        this.addressSender = addressSender;
        this.addressReceiver = addressReceiver;
        this.cadet = cadet;
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
}
