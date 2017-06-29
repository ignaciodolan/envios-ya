
package com.enviosya.login.exceptions;

public class LoginException extends Exception{
    public LoginException(){
        super("Default error");
    }
    
    public LoginException(String message){
        super(message);
    }

}
