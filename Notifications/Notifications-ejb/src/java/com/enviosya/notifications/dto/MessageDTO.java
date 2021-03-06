/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.notifications.dto;

/**
 *
 * @author ignaciodolan
 */
public class MessageDTO {
    
    private String message;
    private String type;

    public MessageDTO(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageToTransfer() {
        StringBuilder messageToTransfer = new StringBuilder();
        messageToTransfer.append("<type>");
        messageToTransfer.append(this.getType());
        messageToTransfer.append("<message>");
        messageToTransfer.append(this.getMessage());
        return messageToTransfer.toString();
    }
    
}
