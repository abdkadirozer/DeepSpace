package com.example.Server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "Users")
@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id",nullable = false)
    private  long id;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name= "password",nullable = false)
    private String password;

    @Column(name= "last_login",nullable = false)
    @UpdateTimestamp
    private LocalDateTime last_login;

    @Column(name = "created_Date",nullable = false)
    @CreationTimestamp
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

    @Column(name = "session_id")
    private String session;

    @Column(name = "salt",nullable = false)
    private byte[] salt;

    @OneToMany(mappedBy="user" ,fetch =  FetchType.LAZY, cascade =  CascadeType.ALL)
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
