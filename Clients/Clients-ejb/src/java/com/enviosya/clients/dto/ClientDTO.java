/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.clients.dto;


import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Marcos
 */
public class ClientDTO {
    private Long id;
    private String document;
    private String name;
    private String lastName;
    private String email;
    private String paymentMethod;
    private String creditCardNumber; 
    private String cvc;
    
    public ClientDTO(Long id, String document, String name, String lastname, 
            String payments, String email, String creditCardNumber, String cvc) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.lastName = lastname;
        this.email = email;
        this.paymentMethod = payments;
        this.creditCardNumber = creditCardNumber;
        this.cvc = cvc;
    }
    
    public ClientDTO(String document, String name, String lastname, 
            String payments, String email, String creditCardNumber, String cvc) {
        this.document = document;
        this.name = name;
        this.lastName = lastname;
        this.email = email;
        this.paymentMethod = payments;
        this.creditCardNumber = creditCardNumber;
        this.cvc = cvc;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
    
}
