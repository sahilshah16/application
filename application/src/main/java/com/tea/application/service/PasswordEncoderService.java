package com.tea.application.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {

    public String hashPassword(String password) {
        // Generate a salt
        String salt = BCrypt.gensalt();

        // Hash the password with the salt
        String hashedPassword = BCrypt.hashpw(password, salt);

        return hashedPassword;
    }

    public boolean checkPassword(String password, String hashedPassword) {
        // Check if the provided password matches the hashed password
        return BCrypt.checkpw(password, hashedPassword);
    }
    
}
