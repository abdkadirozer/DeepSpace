package com.example.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "last_login", nullable = false)
    @UpdateTimestamp
    private LocalDateTime last_login;

    @Column(name = "created_Date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "session_id")
    private String session;

    @Column(name = "salt", nullable = false)
    private byte[] salt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Score> scores = new HashSet<>();

    public static final class UserBuilder {
        private long id;
        private String username;
        private String password;
        private LocalDateTime last_login;
        private LocalDateTime createdDate;
        private String session;
        private byte[] salt;
        private Set<Score> scores = new HashSet<>();

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setLast_login(LocalDateTime last_login) {
            this.last_login = last_login;
            return this;
        }

        public UserBuilder setCreatedDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public UserBuilder setSession(String session) {
            this.session = session;
            return this;
        }

        public UserBuilder setSalt(byte[] salt) {
            this.salt = salt;
            return this;
        }

        public UserBuilder setScores(Set<Score> scores) {
            this.scores = scores;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setLast_login(last_login);
            user.setCreatedDate(createdDate);
            user.setSession(session);
            user.setSalt(salt);
            user.setScores(scores);
            return user;
        }
    }
}
