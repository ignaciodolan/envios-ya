/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.shipments.exceptions;

/**
 *
 * @author ignaciodolan
 */
public class CadetDistanceException extends Exception {

    /**
     * Creates a new instance of <code>CadetDistanceException</code> without
     * detail message.
     */
    public CadetDistanceException() {
    }

    /**
     * Constructs an instance of <code>CadetDistanceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CadetDistanceException(String msg) {
        super(msg);
    }
}
