package com.example.Client.model;

import java.time.LocalDateTime;

public class Score {

    private long score_id;

    private User user;

    public Score() {
    }

    public Score(User user , int point) {
        this.user = user;
        this.point = point;
    }

    private int point;
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
