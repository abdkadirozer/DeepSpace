package com.example.server.service;

import com.example.server.model.User;
import com.example.server.repository.UserJpaRepo;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
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
                user.setSalt(getSalt());
                user.setPassword(getSecurePassword(user.getPassword(), user.getSalt()));
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
            if (input.get("password") != null)
                u.setPassword(getSecurePassword(input.get("password"), u.getSalt()));
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
     * @param user
     * @return session_id which only belongs to the logged in user.
     */
    @Override
    public String login(User user) {
        User temp = this.findUser(user.getUsername());
        if (temp != null) {
            byte[] salt = temp.getSalt();
            String regeneratedPassword = getSecurePassword(user.getPassword(), salt);
            if (temp.getPassword().equals(regeneratedPassword)) {
                String session_id = UUID.randomUUID().toString();
                temp.setSession(session_id);
                temp.setLast_login(LocalDateTime.now());
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

    /**
     * This method implements Java MD5 password hashing with salt.
     *
     * @param passwordToHash
     * @param salt
     * @return hashed version of the password.
     */
    private static String getSecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt

    /**
     * This method is used to get the salt value for password hashing implementation.
     *
     * @return the generated salt value.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }
}
