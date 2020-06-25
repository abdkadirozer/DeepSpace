package com.example.Server.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "Scores")
@Entity

public class Score {

    @Id
    @GeneratedValue
    private long score_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Score() {
    }

    public Score(User user , int point) {
        this.user = user;
        this.point = point;
    }

    @Column(name = "point",nullable = false)
    private int point;

    @CreationTimestamp
    @Column(name = "date",nullable = false)
    private LocalDateTime date;


    public long getScore_id() {
        return score_id;
    }

    public void setScore_id(long score_id) {
        this.score_id = score_id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
