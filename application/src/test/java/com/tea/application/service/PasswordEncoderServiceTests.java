package com.tea.application.service;


import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderServiceTests {


    @Test
    public void testHashPassword() {
       
        String password = "password";

        String hashedPassword = hashPassword(password);

        assertTrue(BCrypt.checkpw(password, hashedPassword));
    }

    @Test
    public void testCheckPassword_CorrectPassword() {
        // Arrange
        String password = "password";
        String hashedPassword = hashPassword(password);

        // Act
        boolean isMatch = checkPassword(password, hashedPassword);

        // Assert
        assertTrue(isMatch);
    }

    private String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    private boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}




