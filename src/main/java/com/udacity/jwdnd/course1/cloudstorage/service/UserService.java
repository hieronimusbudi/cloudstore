package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean checkUsernamePassword(String username, String password){
        String encodedSalt = userMapper.getUsers(username).getSalt();
        String hashedPassword = hashService.getHashedValue(password, encodedSalt);
        return (hashedPassword.equals(userMapper.getUsers(username).getPassword()));
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUsers(username) == null;
    }

    public int createUser(Users user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new Users(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    public Users getUser(String username) {
        return userMapper.getUsers(username);
    }
}
