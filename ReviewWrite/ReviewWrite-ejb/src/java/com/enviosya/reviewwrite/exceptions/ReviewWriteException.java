
package com.enviosya.reviewwrite.exceptions;

public class ReviewWriteException extends Exception{
        public ReviewWriteException(){
        super("Default error");
    }
    
    public ReviewWriteException(String message){
        super(message);
    }
}
