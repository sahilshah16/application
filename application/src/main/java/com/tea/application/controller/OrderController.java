package com.tea.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tea.application.entity.Basket;
import com.tea.application.entity.Order;
import com.tea.application.service.BasketService;
import com.tea.application.service.OrderService;

@Controller
public class OrderController {
    
    @Autowired
    OrderService orderService;

    @Autowired
    BasketService basketService;

    @PostMapping("/handleOrder/{userId}")
    public String handleOrder(@PathVariable String userId, Model model){
        Basket basket = basketService.getBasketByUser(userId);
        Order order= new Order();
        order.setUserId(userId);
        order.setBasketDatas(basket.getBasketDatas());
        orderService.saveOrder(order);
        basketService.deleteBasketByUser(userId);
        return "home";
    }
}
