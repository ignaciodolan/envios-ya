/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.cadets.exceptions;

/**
 *
 * @author Ruso
 */
public class CadetException extends Exception{
    
    public CadetException(){
        super("Default error");
    }
    
    public CadetException(String message){
        super(message);
    }
}
