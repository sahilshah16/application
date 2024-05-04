package com.tea.application.controller;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tea.application.entity.Item;
import com.tea.application.service.ItemService;
import com.tea.application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SearchController {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/search")
    public ResponseEntity<HashSet<Item>> getSearchResults(@RequestParam String searchTerm){

        String[] words = searchTerm.split("\\s+");

        Arrays.stream(words).filter(word -> !word.isEmpty()).toArray(String[]::new);

        return new ResponseEntity<>(itemService.searchArray(words), HttpStatus.OK);
    }

    @GetMapping("/home")
    public String getHome(Model model){

        if (isAuthenticated()) {

            String userId = getUserIdFromSession();
            model.addAttribute("userId", userId);

            return "home"; 
        } else {

            return "redirect:/login";

        }
    }

    @GetMapping("/signOut")
    public String logout() {
        
        HttpSession session = request.getSession(false); 

        if (session != null) {
            session.invalidate(); 
        }
        
        return "redirect:/login"; 
    }

    private boolean isAuthenticated() {
        
        HttpSession session = request.getSession(false);

        return session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated");
    }
    
    private String getUserIdFromSession() {
       
        HttpSession session = request.getSession(false); 
  
        if (session != null && session.getAttribute("userId") != null) {
       
            return (String) session.getAttribute("userId");
        } else {

            return null;
        }
    }
}
