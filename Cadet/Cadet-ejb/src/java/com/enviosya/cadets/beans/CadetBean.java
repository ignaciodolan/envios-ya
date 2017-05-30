/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.cadets.beans;

import com.google.gson.Gson;
import javax.ejb.Stateless;

/**
 *
 * @author Ruso
 */
@Stateless
public class CadetBean implements CadetBeanLocal {
    
    private final Gson gson = new Gson();
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
