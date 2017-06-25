package com.enviosya.shipments.exceptions;

public class CalculateCostException extends Exception {

    public CalculateCostException() {
        super("Default error");
    }

    public CalculateCostException(String msg) {
        super(msg);
    }
}
