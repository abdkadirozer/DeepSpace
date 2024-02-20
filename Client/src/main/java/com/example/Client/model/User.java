package com.example.Client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private  long id;
    private String username;
    private String password;
    private LocalDateTime last_login;
    private LocalDateTime createdDate;
    private String session;
    private String salt;
    private Set<Score> scores = new HashSet<>();

    public User(
             @JsonProperty(value = "username") String username,
             @JsonProperty(value = "password") String password) {
        this.username = username;
        this.password = password;
    }
}
