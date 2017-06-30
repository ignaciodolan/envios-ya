
package com.enviosya.reviewread.exceptions;

public class ReviewReadException extends Exception{
        public ReviewReadException(){
        super("Default error");
    }
    
    public ReviewReadException(String message){
        super(message);
    }
}
