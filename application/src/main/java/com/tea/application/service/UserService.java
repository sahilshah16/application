package com.tea.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tea.application.entity.User;
import com.tea.application.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user){
        String hashedPassword = passwordEncoderService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    } 

    public boolean authenticate(String password, String hashedPassword){
        return passwordEncoderService.hashPassword(password).equals(hashedPassword);
    }
}