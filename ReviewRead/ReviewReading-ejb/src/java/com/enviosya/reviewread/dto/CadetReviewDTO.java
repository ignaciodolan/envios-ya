
package com.enviosya.reviewread.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CadetReviewDTO {
    
    private String document;

    private String name;

    private String lastName;

    private String email;

    private List<ReviewDTO> reviews;

    private Date createdDate;

    public CadetReviewDTO() {
        reviews = new ArrayList<>();
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    
    
}
