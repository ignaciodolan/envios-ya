

package com.enviosya.reviewcheckclient.dto;

import java.io.Serializable;

public class ReviewDTO implements Serializable{
    private int calification;
    private String comment;
    private int status;
    private int feeling;
    private Long shipmentId;
    private Long clientId;

    public ReviewDTO() {
    }

    public ReviewDTO(int calification, String comment, int status, int feeling, Long shipmentId, Long clientId) {
        this.calification = calification;
        this.comment = comment;
        this.status = status;
        this.feeling = feeling;
        this.shipmentId = shipmentId;
        this.clientId = clientId;
    }

    
    
    public int getCalification() {
        return calification;
    }
    
    

    public void setCalification(int calification) {
        this.calification = calification;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    
    
    

}
