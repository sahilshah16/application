package com.tea.application.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {

    public String hashPassword(String password) {
       
        String salt = BCrypt.gensalt();

        String hashedPassword = BCrypt.hashpw(password, salt);

        return hashedPassword;
    }

    public boolean checkPassword(String password, String hashedPassword) {
       
        return BCrypt.checkpw(password, hashedPassword);
    }
    
}
