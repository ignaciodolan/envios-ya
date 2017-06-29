/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.review.exceptions;

/**
 *
 * @author ignaciodolan
 */
public class ReviewException extends Exception {

    /**
     * Creates a new instance of <code>ReviewException</code> without detail
     * message.
     */
    public ReviewException() {
    }

    /**
     * Constructs an instance of <code>ReviewException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ReviewException(String msg) {
        super(msg);
    }
}
