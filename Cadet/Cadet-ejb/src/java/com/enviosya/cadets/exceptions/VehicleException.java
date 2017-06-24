package com.enviosya.cadets.exceptions;

public class VehicleException extends Exception{
    

    public VehicleException(){
        super("Default error");
    }
    
    public VehicleException(String message){
        super(message);
    }
}