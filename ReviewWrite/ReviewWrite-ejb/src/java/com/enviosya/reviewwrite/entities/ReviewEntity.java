/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.reviewwrite.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Ruso
 */
@Entity
public class ReviewEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int rating;
    private String comment;
    private ReviewStatus reviewStatus;
    private ReviewFeeling reviewFeeling;
    private Long shimpentId;
    private Long clientId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;

    public ReviewEntity() {
    }

    public ReviewEntity(int rating, String comment, ReviewStatus reviewStatus, ReviewFeeling reviewFeeling, Long shimpentId, Long clientId, Date createdDate) {
        this.rating = rating;
        this.comment = comment;
        this.reviewStatus = reviewStatus;
        this.reviewFeeling = reviewFeeling;
        this.shimpentId = shimpentId;
        this.clientId = clientId;
        this.createdDate = createdDate;
    }

    
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public ReviewFeeling getReviewFeeling() {
        return reviewFeeling;
    }

    public void setReviewFeeling(ReviewFeeling reviewFeeling) {
        this.reviewFeeling = reviewFeeling;
    }

    public Long getShimpentId() {
        return shimpentId;
    }

    public void setShimpentId(Long shimpentId) {
        this.shimpentId = shimpentId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
        if (!(object instanceof ReviewEntity)) {
            return false;
        }
        ReviewEntity other = (ReviewEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.enviosya.reviewwrite.dto.ReviewEntity[ id=" + id + " ]";
    }
    
}
