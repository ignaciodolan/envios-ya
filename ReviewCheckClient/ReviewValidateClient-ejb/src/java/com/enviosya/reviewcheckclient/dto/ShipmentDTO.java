
package com.enviosya.reviewcheckclient.dto;


public class ShipmentDTO {
    private Long clientSender;
    private Long clientReceiver;

    public ShipmentDTO() {
    }

    public ShipmentDTO(Long clientSender, Long clientReceiver) {
        this.clientSender = clientSender;
        this.clientReceiver = clientReceiver;
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

    
}
