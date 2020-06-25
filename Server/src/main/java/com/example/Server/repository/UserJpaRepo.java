package com.example.Server.repository;

import com.example.Server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserJpaRepo extends JpaRepository<User,Long> {
    User findBySession(String session);
}
