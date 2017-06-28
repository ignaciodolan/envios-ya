
package com.enviosya.login.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Ruso
 */
@Entity
public class LoginEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String token;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdTokenDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date tokenExpirationDate;
    private String permission;

    public String getUser() {
        return userName;
    }

    public void setUser(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getStartDate() {
        return createdTokenDate;
    }

    public void setStartDate(Date createdTokenDate) {
        this.createdTokenDate = createdTokenDate;
    }

    public Date getEndDate() {
        return tokenExpirationDate;
    }

    public void setEndDate(Date tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    

    public LoginEntity() {
    }

    public LoginEntity(String userName, String token, Date createdTokenDate, Date tokenExpirationDate, String permission) {
        this.userName = userName;
        this.token = token;
        this.createdTokenDate = createdTokenDate;
        this.tokenExpirationDate = tokenExpirationDate;
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoginEntity)) {
            return false;
        }
        LoginEntity other = (LoginEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.enviosya.login.entities.LoginEntity[ id=" + id + " ]";
    }
    
}
