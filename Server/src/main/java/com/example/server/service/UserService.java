package com.example.server.service;

import com.example.server.model.User;
import com.example.server.model.request.UserAddRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    User addUser(User user);
    List<User> getAllUsers();
    User deleteUser(String username,String session);
    User findUser(String username);
    String login(UserAddRequest user);
    User updateUser(Map<String, String> input);
}
