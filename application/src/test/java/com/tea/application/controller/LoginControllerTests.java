package com.tea.application.controller;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.tea.application.entity.User;
import com.tea.application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTests {
    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @InjectMocks
    private LoginController loginController;


    @BeforeEach
    public void resetMocks() {
        Mockito.reset(userService, session, request, model);
    }
    @Test
    public void testSubmitUser_UsernameNotFound() {

        User user = new User("username", "password");

        when(userService.getUserByUsername(user.getUsername())).thenReturn(null);

        String viewName = loginController.submitUser(user, model);

        assertEquals("login", viewName);

        verify(model).addAttribute("errorMessage", "Username does not exist");
    }
    
    @Test
    public void testSubmitUser_InvalidPassword() {

        User user = new User("username", "password");

        User existingUser = new User("username", "hashedPassword");

        when(userService.getUserByUsername(user.getUsername())).thenReturn(existingUser);

        when(userService.authenticate(user.getPassword(), existingUser.getPassword())).thenReturn(false);

        String viewName = loginController.submitUser(user, model);

        assertEquals("login", viewName);

        verify(model).addAttribute(eq("errorMessage"), eq("Invalid password"));

    }
    
    @Test
    public void testSubmitUser_Success() throws Exception {
        
        User user = new User("username", "password");
        User existingUser = new User("id","username", "hashedPassword");

        when(userService.getUserByUsername(user.getUsername())).thenReturn(existingUser);

        when(userService.authenticate(user.getPassword(), existingUser.getPassword())).thenReturn(true);

        when(request.getSession()).thenReturn(session);

        String viewName = loginController.submitUser(user, model);

        assertEquals("redirect:/home", viewName);

        verify(session).setAttribute("authenticated", true);

        verify(session).setAttribute("userId", "id");
       
    }
    
    @Test
    public void testAddNewUser_ShortPassword() {
        User user = new User("username", "short");
        when(userService.getUserByUsername(user.getUsername())).thenReturn(null);

        String viewName = loginController.addNewUser(user, model);

        assertEquals("registration", viewName);
        verify(model).addAttribute("errorMessage","Password Must Be At Least 6 Characters");
        verify(userService, never()).saveUser(user); 
    }

    @Test
    public void testAddNewUser_Success() {
        User user = new User("username", "strongPassword");
        when(userService.getUserByUsername(user.getUsername())).thenReturn(null);

        String viewName = loginController.addNewUser(user, model);

        assertEquals("login", viewName);
        verify(model).addAttribute("user",user);

        verify(model).addAttribute("success", "true");
        verify(userService).saveUser(user);
    }

    @Test
    public void testAddNewUser_UsernameExists() {
        User user = new User("username", "password");
        User existingUser = new User("username", "existingPassword");
        when(userService.getUserByUsername(user.getUsername())).thenReturn(existingUser);

        String viewName = loginController.addNewUser(user, model);

        assertEquals("registration", viewName);
        verify(model).addAttribute("errorMessage", "Username already exists");
        verify(userService, never()).saveUser(user); // Verify saveUser is not called
    }

}



