package com.example.Client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class User {
    private  long id;

    private String username;
    private String password;

    private LocalDateTime last_login;

    private LocalDateTime createdDate;


    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
    private String session;

    private byte[] salt;
    private Set<Score> scores = new HashSet<>();

    public User() {
    }

    public User(
             @JsonProperty(value = "username") String username,
             @JsonProperty(value = "password") String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLast_login() {
        return last_login;
    }

    public void setLast_login(LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
