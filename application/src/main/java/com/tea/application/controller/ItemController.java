package com.tea.application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tea.application.entity.Item;
import com.tea.application.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    private HttpServletRequest request;
    
    @GetMapping("/item/{itemId}")
    public String getItemInfo(@PathVariable String itemId, Model model){
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            
            if (session.getAttribute("userId") != null) {
       
                String userId= (String) session.getAttribute("userId");

                model.addAttribute("userId", userId);
            }
        }
        Optional<Item> itemOptional = itemService.searchById(itemId);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            model.addAttribute("item", item);
        }
        else{
            model.addAttribute("noItem", true);
        }
        return "item";
    }
}
