package com.tea.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tea.application.entity.User;
import com.tea.application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/login")
    public String getLogin(Model model){

        model.addAttribute("user", new User());

        return "login";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model){

        model.addAttribute("user", new User());

        return "registration";
    }


    @PostMapping("/handleLogin")
    public String submitUser(User user, Model model){

        User validateUsername = userService.getUserByUsername(user.getUsername());

        if(validateUsername==null){

            model.addAttribute("errorMessage", "Username does not exist");
            return "login";

        }
        else if(userService.authenticate(user.getPassword(), validateUsername.getPassword())){

            model.addAttribute("success", "true");

            HttpSession session = request.getSession();
            session.setAttribute("authenticated", true);
            session.setAttribute("userId", validateUsername.getId());

            return "redirect:/home";
            
        }

        else{

            model.addAttribute("errorMessage", "Invalid password");

            return "login";
        }
        
    }

    @PostMapping("/handleRegister")
    public String addNewUser(User user, Model model){

        User validateUsername = userService.getUserByUsername(user.getUsername());

        if(validateUsername==null){

            userService.saveUser(user);

            model.addAttribute("user", user);
            model.addAttribute("success", "true");

            return "login";
        }
        else{

            model.addAttribute("errorMessage", "Username already exists");

            return "registration";
        }
    }

    
}