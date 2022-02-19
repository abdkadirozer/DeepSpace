package com.example.server.service;

import com.example.server.model.Score;
import com.example.server.model.projection.ScoreBoardDto;
import com.example.server.model.projection.ScoreDto;

import java.util.List;

public interface ScoreService {
    List<ScoreDto> getAll();

    Score addScore(Score score);
    ScoreDto findMaxByName(String name);

    List<ScoreBoardDto> getBoard();
    List<ScoreBoardDto> getWeekBoard();
    List<ScoreBoardDto> getMonthBoard();

}
