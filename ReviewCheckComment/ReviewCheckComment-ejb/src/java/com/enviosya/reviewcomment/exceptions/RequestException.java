
package com.enviosya.reviewcomment.exceptions;

public class RequestException extends Exception{
    
    public RequestException(){
        super("Default error");
    }
    
    public RequestException(String message){
        super(message);
    }
}
