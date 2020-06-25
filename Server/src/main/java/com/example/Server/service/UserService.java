package com.example.Server.service;

import com.example.Server.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User addUser(User user);
    List<User> getAllUsers();
    User deleteUser(String username,String session);
    User findUser(String username);
    String login(User user);
    User updateUser(Map<String, String> input);
}
