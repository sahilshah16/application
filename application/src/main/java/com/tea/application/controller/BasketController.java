package com.tea.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BasketController {

    @GetMapping("/addToBasket/{itemId}")
    public ResponseEntity<String> addToBasket(@PathVariable String itemId,@RequestParam(required = false) Integer quantity, Model model){
        System.out.println(quantity);
        return new ResponseEntity<>("Here", HttpStatus.OK);
    }
    
}
