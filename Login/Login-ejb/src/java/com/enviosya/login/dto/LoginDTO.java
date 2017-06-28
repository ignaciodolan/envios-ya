package com.enviosya.login.dto;


import java.util.Date;


public class LoginDTO {
    
    private String userName;
        
    private String token;
    
    private Date createdTokenDate;
    
    private Date lastConnectionDate;
    

    public LoginDTO() {
    }

    public LoginDTO(String userName, String token, Date createdTokenDate, Date lastConnectionDate) {
        this.userName = userName;
        this.token = token;
        this.createdTokenDate = createdTokenDate;
        this.lastConnectionDate = lastConnectionDate;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedTokenDate() {
        return createdTokenDate;
    }

    public void setCreatedTokenDate(Date createdTokenDate) {
        this.createdTokenDate = createdTokenDate;
    }

    public Date getLastConnectionDate() {
        return lastConnectionDate;
    }

    public void setLastConnectionDate(Date lastConnectionDate) {
        this.lastConnectionDate = lastConnectionDate;
    }    


}

