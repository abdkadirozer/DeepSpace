package com.example.server.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "Scores")
@Entity
public class Score {

    @Id
    @GeneratedValue
    private long score_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "point", nullable = false)
    private int point;

    @CreationTimestamp
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public Score(User user, int point) {
        this.user = user;
        this.point = point;
    }
}
