package com.example.server.service;

import com.example.server.model.User;
import com.example.server.model.request.UserAddRequest;
import com.example.server.repository.UserJpaRepo;
import com.example.server.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service

public class UserServiceImpl implements UserService {

    private final UserJpaRepo userJpaRepo;

    public UserServiceImpl(UserJpaRepo userJpaRepo) {
        this.userJpaRepo = userJpaRepo;
    }

    /**
     * This method is used to add a new user to the database.
     *
     * @param user
     * @return the added user which is added to the database.
     */
    @Override
    public User addUser(User user) {
        if (findUser(user.getUsername()) == null) {
            try {
                byte[] salt = SecurityUtils.generateSalt();
                user.setSalt(SecurityUtils.toHex(salt));
                user.setPassword(SecurityUtils.generateSecurePassword(user.getPassword(), salt));
                user = userJpaRepo.save(user);
            } catch (Exception e) {
                return null;
            }
            return user;
        }
        return null;
    }

    /**
     * This method is used to list all of the users in the database.
     *
     * @return all of the existing users in the database.
     */
    @Override
    public List<User> getAllUsers() {
        return userJpaRepo.findAll();
    }

    /**
     * This method is used to update, i.e., change its username or password, information of the user.
     *
     * @param input a map which contains username, password, and session information of the user.
     * @return the user with the updated attributes.
     */
    @Override
    public User updateUser(Map<String, String> input) {
        User u = userJpaRepo.findBySession(input.get("session")).orElse(null);
        if (u != null) {
            if (input.get("username") != null)
                u.setUsername(input.get("username"));
            if (input.get("password") != null){
                byte[] salt = SecurityUtils.fromHex(u.getSalt());
                u.setPassword(SecurityUtils.generateSecurePassword(input.get("password"), salt));
            }
        }
        assert u != null;
        return userJpaRepo.save(u);
    }

    /**
     * This method is used to delete the user with given username and session information.
     *
     * @param username
     * @param session
     * @return the deleted user.
     */
    @Override
    public User deleteUser(String username, String session) {
        if (session == null)
            return null;
        User u = userJpaRepo.findBySession(session).orElse(null);

        if (u != null && u.getSession().equals(session)) {
            userJpaRepo.delete(u);
        }
        return u;
    }

    /**
     * This method is used to provide login functionality to the user.
     *
     * @param request
     * @return session_id which only belongs to the logged in user.
     */
    @Override
    public String login(UserAddRequest request) {
        User temp = this.findUser(request.getUsername());
        if (temp != null) {
            String regeneratedPassword = SecurityUtils.generateSecurePassword(request.getPassword(), SecurityUtils.fromHex(temp.getSalt()));
            if (temp.getPassword().equals(regeneratedPassword)) {
                String session_id = UUID.randomUUID().toString();
                temp.setSession(session_id);
                temp.setLastLogin(LocalDateTime.now());
                userJpaRepo.save(temp);
                return session_id;
            }
        }
        return null;
    }

    /**
     * This method is used to find the user with the given username.
     *
     * @param username
     * @return the found user.
     */
    @Override
    public User findUser(String username) {
        return userJpaRepo.findByUsername(username).orElse(null);
    }
}
