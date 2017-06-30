/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.reviewread.exceptions;

/**
 *
 * @author Ruso
 */
public class RequestException extends Exception{
    
    public RequestException(){
        super("Default error");
    }
    
    public RequestException(String message){
        super(message);
    }
}
