package com.enviosya.cadets.exceptions;

public class CadetException extends Exception{
    
    public CadetException(){
        super("Default error");
    }
    
    public CadetException(String message){
        super(message);
    }
}
