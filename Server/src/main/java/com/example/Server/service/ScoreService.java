package com.example.Server.service;

import com.example.Server.model.Score;

import java.util.List;
import java.util.Map;

public interface ScoreService {
    List<Map<String,String>> getAll();
    List<Map<String,String>> getBoard();

    Score addScore(Score score);
    Map<String,String> findMaxByName(String name);
    List<Map<String,String>> getWeekBoard();
    List<Map<String,String>> getMonthBoard();

}
