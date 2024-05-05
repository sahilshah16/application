package com.tea.application.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tea.application.entity.Basket;
import com.tea.application.entity.BasketData;
import com.tea.application.entity.Item;
import com.tea.application.service.BasketService;
import com.tea.application.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BasketController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    BasketService basketService;

    @Autowired
    ItemService itemService;

    @PostMapping("/addToBasket/{itemId}")
    public String addToBasket(@PathVariable String itemId,@RequestParam(required = false) Integer quantity, Model model){
        
        HttpSession session = request.getSession(false);
            
        String userId= (String) session.getAttribute("userId");
        if(basketService.getBasketByUser(userId)==null){
            Basket basket = new Basket();
            basket.setUserId(userId);
            List<BasketData> basketDatas = new ArrayList<>();
            BasketData basketData = new BasketData();
            basketData.setQuantity(quantity);
            Optional<Item> itemOptional = itemService.searchById(itemId);
            if (itemOptional.isPresent()) {
                Item item = itemOptional.get();
                basketData.setItem(item);
            }
            basketDatas.add(basketData);
            basket.setBasketDatas(basketDatas);
            basketService.saveBasket(basket);
        }
        else{
            Basket basket = basketService.getBasketByUser(userId);
            BasketData basketData = new BasketData();
            basketData.setQuantity(quantity);
            Optional<Item> itemOptional = itemService.searchById(itemId);
            if (itemOptional.isPresent()) {
                Item item = itemOptional.get();
                basketData.setItem(item);
            }
            List<BasketData> basketDatas=basket.getBasketDatas();
            basketDatas.add(basketData);
            basket.setBasketDatas(basketDatas);
            basketService.saveBasket(basket);
        }
        model.addAttribute("basket", true);
        return "registration";
    } 
        
        
}

