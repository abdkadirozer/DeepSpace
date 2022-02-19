package com.example.server.service;

import com.example.server.model.Score;
import com.example.server.model.projection.ScoreBoardDto;
import com.example.server.model.projection.ScoreDto;
import com.example.server.repository.ScoreJpaRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    private final ScoreJpaRepo scoreJpaRepo;

    public ScoreServiceImpl(ScoreJpaRepo scoreJpaRepo) {
        this.scoreJpaRepo = scoreJpaRepo;
    }


    /**
     * This method is used to list all scores of the users.
     * @return list of map which contains score entities of all users.
     */
    @Override
    public List<ScoreDto> getAll() {
        var scores = scoreJpaRepo.findAllScores();

        return scores;
    }

    /**
     * This method is used to list max scores of all users.
     * @return list of map which contains max score entities of all users.
     */
    @Override
    public List<ScoreBoardDto> getBoard() {
        return scoreJpaRepo.getBoard();
    }

    /**
     * This method is used to find the max score of a user with the given username.
     * @param username
     * @return a map which contains the score entity of the user with the given username.
     */
    @Override
    public ScoreDto findMaxByName(String username) {
        return scoreJpaRepo.getMaxScoreByUsername(username);
    }

    /**
     * This method is used to list max scores of all users weekly.
     * @return list of map which contains max score entities of all users for last 7 days.
     */
    @Override
    public List<ScoreBoardDto> getWeekBoard() {
        return  scoreJpaRepo.getWeeklyBoard();
    }

    @Override
    public List<ScoreBoardDto> getMonthBoard() {
        return scoreJpaRepo.getMonthlyBoard();
    }
    /**
     * This method is used to add a new score log to the database.
     * @param score
     * @return the score entity which is added to the database.
     */
    @Override
    public Score addScore(Score score) {
        return scoreJpaRepo.save(score);
    }
}
