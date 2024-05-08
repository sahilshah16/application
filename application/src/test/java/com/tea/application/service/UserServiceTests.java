package com.tea.application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tea.application.entity.User;
import com.tea.application.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserByUsername() {
        // Arrange
        String username = "username";
        User expectedUser = new User();
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        // Act
        User actualUser = userService.getUserByUsername(username);

        // Assert
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetUserById_UserFound_ReturnsUser() {
        // Arrange
        String userId = "userId";
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> actualUser = userService.getUserById(userId);

        // Assert
        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    public void testGetUserById_UserNotFound_ReturnsEmptyOptional() {
        // Arrange
        String userId = "nonExistingUserId";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> actualUser = userService.getUserById(userId);

        // Assert
        assertFalse(actualUser.isPresent());
    }

    @Test
    public void testSaveUser() {
        // Arrange
        User user = new User();
        user.setPassword("password"); // Setting a password for the user
        String hashedPassword = "hashedPassword";
        when(passwordEncoderService.hashPassword(anyString())).thenReturn(hashedPassword);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals(user, savedUser);
        verify(passwordEncoderService).hashPassword(anyString()); // Using argument matcher to match any string
        verify(userRepository).save(user);
    }



    @Test
    public void testAuthenticate_CorrectPassword_ReturnsTrue() {
        // Arrange
        String password = "password";
        String hashedPassword = "hashedPassword";
        when(passwordEncoderService.checkPassword(password, hashedPassword)).thenReturn(true);

        // Act
        boolean isAuthenticated = userService.authenticate(password, hashedPassword);

        // Assert
        assertTrue(isAuthenticated);
    }

    @Test
    public void testAuthenticate_IncorrectPassword_ReturnsFalse() {
        // Arrange
        String password = "password";
        String hashedPassword = "hashedPassword";
        when(passwordEncoderService.checkPassword(password, hashedPassword)).thenReturn(false);

        // Act
        boolean isAuthenticated = userService.authenticate(password, hashedPassword);

        // Assert
        assertFalse(isAuthenticated);
    }
}

