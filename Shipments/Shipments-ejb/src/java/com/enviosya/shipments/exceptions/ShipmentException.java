package com.enviosya.shipments.exceptions;

public class ShipmentException extends Exception{
    
    public ShipmentException(){
        super("Default error");
    }
    
    public ShipmentException(String message){
        super(message);
    }
    
}
