package com.enviosya.clients.exceptions;


public class ClientException extends Exception {
    private static final long serialVersionUID = 1L;

    public ClientException(String mensaje) {
        super(mensaje);
    }
    
}
