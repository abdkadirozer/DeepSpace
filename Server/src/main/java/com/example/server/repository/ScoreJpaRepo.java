package com.example.server.repository;

import com.example.server.model.Score;
import com.example.server.model.projection.ScoreBoardDto;
import com.example.server.model.projection.ScoreDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreJpaRepo extends JpaRepository<Score, Long> {
    @Query(value = "SELECT new com.example.server.model.projection.ScoreDto(s.score_id,s.user.username,s.point,s.date) FROM Score as s")
    List<ScoreDto> findAllScores();

    @Query(value = "SELECT new com.example.server.model.projection.ScoreBoardDto(s.user.username,max(s.point),s.date) FROM Score as s GROUP BY s.user")
    List<ScoreBoardDto> getBoard();

    @Query(value = "SELECT new com.example.server.model.projection.ScoreBoardDto(s.user.username,max(s.point),s.date) FROM Score as s GROUP BY s.user")
    List<ScoreBoardDto> getWeeklyBoard();

    @Query(value = "SELECT new com.example.server.model.projection.ScoreBoardDto(s.user.username,max(s.point),s.date) FROM Score as s GROUP BY s.user")
    List<ScoreBoardDto> getMonthlyBoard();

    @Query(value = "SELECT new com.example.server.model.projection.ScoreDto(s.score_id,s.user.username,max(s.point),s.date) FROM Score as s WHERE s.user.username=:username GROUP BY s.user")
    ScoreDto getMaxScoreByUsername(String username);
}
