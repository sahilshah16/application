package com.tea.application.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    public String getSearchResults(@RequestParam String searchTerm, Model model){

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            
            if (session.getAttribute("userId") != null) {
       
                String userId= (String) session.getAttribute("userId");

                model.addAttribute("userId", userId);
            }
        }

        String[] words = searchTerm.split("\\s+");

        Arrays.stream(words).filter(word -> !word.isEmpty()).toArray(String[]::new);

        model.addAttribute("searchResults", itemService.searchArray(words));

        model.addAttribute("afterSearch", true);

        System.out.println(model.getAttribute("afterSearch"));

        return "home";
    }

    @GetMapping("/home")
    public String getHome(Model model){

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            
            if (session.getAttribute("userId") != null) {
    
                String userId= (String) session.getAttribute("userId");

                model.addAttribute("userId", userId);

                model.addAttribute("afterSearch", false);

                return "home"; 
            } 

        } 

        return "redirect:/login";
    }

    @GetMapping("/signOut")
    public String logout() {
        
        HttpSession session = request.getSession(false); 

        if (session != null) {
            session.invalidate(); 
        }

        return "redirect:/login"; 
    }

}
