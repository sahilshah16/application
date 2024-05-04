package com.tea.application.utils;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SessionUtils {

    @Autowired
    HttpServletRequest request;
    
    public boolean isAuthenticated() {
        
        HttpSession session = request.getSession(false);

        return session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated");
    }
    
    public String getUserIdFromSession() {
       
        HttpSession session = request.getSession(false); 
  
        if (session != null && session.getAttribute("userId") != null) {
       
            return (String) session.getAttribute("userId");
        } else {

            return null;
        }
    }

}
