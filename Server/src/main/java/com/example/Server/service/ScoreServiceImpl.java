package com.example.Server.service;

import com.example.Server.model.Score;
import com.example.Server.repository.ScoreJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreJpaRepo scoreJpaRepo;
    @PersistenceContext
    private EntityManager em;


    /**
     * This method is used to list all scores of the users.
     * @return list of map which contains score entities of all users.
     */
    @Override
    public List<Map<String,String>> getAll() {
        Query q = em.createNativeQuery("SELECT scores.score_id, users.username, scores.point, scores.date " +
                "FROM scores  " +
                "INNER JOIN users ON scores.user_id= users.id");
        List<Object[]> list = q.getResultList();
        HashMap<String,String> row;
        List<Map<String,String>> result = new ArrayList<>();
        for(Object[] o : list)
        {
            row = new HashMap<>();
                row.put("score_id", o[0].toString());
                row.put("username",o[1].toString());
                row.put("point",o[2].toString());
                row.put("date",o[3].toString());
                result.add(row);
        }
        return result;
    }

    /**
     * This method is used to list max scores of all users.
     * @return list of map which contains max score entities of all users.
     */
    @Override
    public List<Map<String, String>> getBoard() {
        Query q = em.createNativeQuery("SELECT u.username, MAX(point) as maks, s.date FROM " +
                "scores s,users u where s.user_id = u.id group by s.user_id order by maks desc;");
        List<Object[]> list = q.getResultList();
        HashMap<String,String> row;
        List<Map<String,String>> result = new ArrayList<>();
        for(Object[] o : list)
        {
            row = new HashMap<>();
            row.put("username",o[0].toString());
            row.put("point",o[1].toString());
            row.put("date",o[2].toString());
            result.add(row);
        }
        return result;
    }

    /**
     * This method is used to find the max score of a user with the given username.
     * @param username
     * @return a map which contains the score entity of the user with the given username.
     */
    @Override
    public Map<String,String> findMaxByName(String username) {
        Query q = em.createNativeQuery("SELECT s.score_id , s.date ,  s.user_id ,  MAX(point) FROM " + "scores s,users u where s.user_id = u.id  and u.username= '" + username + "' ");
        List<Object[]> list = q.getResultList();
        HashMap<String,String> row = null;
        for(Object[] o : list)
        {
            row = new HashMap<>();
            row.put("score_id",o[0].toString());
            row.put("date",o[1].toString());
            row.put("user_id",o[2].toString());
            row.put("point",o[3].toString());
        }
        return row;
    }

    /**
     * This method is used to list max scores of all users weekly.
     * @return list of map which contains max score entities of all users for last 7 days.
     */
    @Override
    public List<Map<String, String>> getWeekBoard() {
        Query q = em.createNativeQuery("SELECT u.username, MAX(point) as maks, s.date FROM " +
                "scores s,users u where s.date >= date_sub(now(),interval 7 day) and s.user_id = u.id group by s.user_id order by maks desc;");
        List<Object[]> list = q.getResultList();
        HashMap<String,String> row;
        List<Map<String,String>> result = new ArrayList<>();
        for(Object[] o : list)
        {
            row = new HashMap<>();
            row.put("username",o[0].toString());
            row.put("point",o[1].toString());
            row.put("date",o[2].toString());
            result.add(row);
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getMonthBoard() {
        Query q = em.createNativeQuery("SELECT u.username, MAX(point) as maks, s.date FROM " +
                "scores s,users u where s.date >= date_sub(now(),interval 30 day) and s.user_id = u.id group by s.user_id order by maks desc;");
        List<Object[]> list = q.getResultList();
        HashMap<String,String> row;
        List<Map<String,String>> result = new ArrayList<>();
        for(Object[] o : list)
        {
            row = new HashMap<>();
            row.put("username",o[0].toString());
            row.put("point",o[1].toString());
            row.put("date",o[2].toString());
            result.add(row);
        }
        return result;
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
