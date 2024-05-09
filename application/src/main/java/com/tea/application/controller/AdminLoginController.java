package com.tea.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tea.application.entity.Admin;
import com.tea.application.entity.Item;
import com.tea.application.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class AdminLoginController {

    @Autowired
    AdminService adminService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/adminLogin")
    public String getLoginAdmin(Model model){

        model.addAttribute("admin", new Admin());

        return "adminLogin";
    }

    @PostMapping("/handleAdmin")
    public String submitAdmin(Admin admin, Model model){

        Admin validateAdmin = adminService.getAdminByUsername(admin.getUsername());

        if(validateAdmin==null){

            model.addAttribute("errorMessage", "Username does not exist");
            return "adminLogin";

        }
        else if(adminService.authenticate(admin.getPassword(), validateAdmin.getPassword())){

            model.addAttribute("success", "true");

            HttpSession session = request.getSession();
            session.setAttribute("authenticated", true);
            session.setAttribute("adminId", validateAdmin.getId());
            model.addAttribute("adminId", validateAdmin.getId());
            model.addAttribute("item", new Item());
            return "adminHome";
            
        }

        else{

            model.addAttribute("errorMessage", "Invalid password");

            return "adminLogin";
        }
        
    }
    @GetMapping("/signOutAdmin")
    public String logout(Model model) {
        
        HttpSession session = request.getSession(false); 

        if (session != null) {
            session.invalidate(); 
        }

        model.addAttribute("admin", new Admin());
        
        return "adminLogin"; 
    }

    @GetMapping("/adminHome")
    public String adminHome(Model model){
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            
            if (session.getAttribute("adminId") != null) {
       
                String adminId= (String) session.getAttribute("adminId");

                model.addAttribute("adminId", adminId);
            }
        }
        model.addAttribute("item", new Item());
        return "adminHome";
    }
}
