package com.example.Client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Score {

    private long score_id;

    private User user;

    private int point;
    private LocalDateTime date;
}
