/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.shipments.domain;

/**
 *
 * @author ignaciodolan
 */
public class PackageNormal extends Package{

    @Override
    public Double getCost() {
        return (this.getHeight() * this.getLength() / 2) + this.getWeight();
    }
    
}
