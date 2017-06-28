package com.enviosya.shipments.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class ShipmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String description;
    @NotNull
    private Long clientSender;
    @NotNull
    private Long clientReceiver;
    @NotNull
    private String addressSender;
    @NotNull
    private String addressReceiver;
    private Long cadet;
    private Double cost;
    private Double comission;
    
    @Column(length = 4096)
    private String packagePhoto;

    public ShipmentEntity(String description, Long clientSender, Long clientReceiver, String addressSender, String addressReceiver, Long cadet, String packagePhoto, Double cost, Double comission) {
        this.description = description;
        this.clientSender = clientSender;
        this.clientReceiver = clientReceiver;
        this.addressSender = addressSender;
        this.addressReceiver = addressReceiver;
        this.cadet = cadet;
        this.packagePhoto = packagePhoto;
        this.comission = comission;
        this.cost = cost;
    }

    public ShipmentEntity() {
        
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

    public String getPackagePhoto() {
        return packagePhoto;
    }

    public void setPackagePhoto(String packagePhoto) {
        this.packagePhoto = packagePhoto;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getComission() {
        return comission;
    }

    public void setComission(Double comission) {
        this.comission = comission;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShipmentEntity)) {
            return false;
        }
        ShipmentEntity other = (ShipmentEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.enviosya.shipments.entities.ShipmentEntity[ id=" + id + " ]";
    }
    
}
