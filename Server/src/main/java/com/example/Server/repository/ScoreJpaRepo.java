package com.example.Server.repository;

import com.example.Server.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreJpaRepo extends JpaRepository<Score,Long> {
}
