
package com.enviosya.cadets.entities;

import com.enviosya.cadets.vehicles.entities.VehicleEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ruso
 */
@Entity
@XmlRootElement
public class CadetEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Column (unique = true)
    private String document;
    
    @NotNull 
    private String name;
    
    @NotNull
    private String surname;

    @NotNull
    @Column(unique = true)
    private String email;
    
    @OneToMany
    private List<VehicleEntity>vehicles;
    public CadetEntity () {
    
    }   
    public CadetEntity(Long id, String document, String name, String surname, String email, List<VehicleEntity> vehicles) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.vehicles = vehicles;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final CadetEntity other = (CadetEntity) object;
        return Objects.equals(this.id, other.id);
    }
   
}
