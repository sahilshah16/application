package com.tea.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
        model.addAttribute("userId", userId);

        
        return "home";
    } 
    
    @GetMapping("/basket/{userId}")
    public String getBasket(@PathVariable String userId, Model model){
        Basket basket = basketService.getBasketByUser(userId);
        if(basket==null){
            model.addAttribute("emptyBasket", true);
            return "basket";
        }
        else{
            model.addAttribute("userId", userId);
            model.addAttribute("basketData", basket.getBasketDatas());
            List<Double> prices = new ArrayList<>();
            double price_total =0.0;
            double weight =0.0;
            for(BasketData data: basket.getBasketDatas()){
                double quantityWithPrice = data.getQuantity()*data.getItem().getItemPriceGBP();
                double quantityWithWeight = data.getQuantity()*data.getItem().getAmountInGrams();
                price_total+=quantityWithPrice;
                weight+=quantityWithWeight;
                prices.add(quantityWithPrice);
            }
            double shipping_weight=0.00;
            if(price_total>100){
                shipping_weight=0.00;
            }
            else if(weight<6000){
                shipping_weight=1.50;
            }
            else if(weight>=6000 && weight<=10000){
                shipping_weight=10.00;
            }
            else if(weight>=10000){
                shipping_weight=15.00;
            }
            model.addAttribute("shipping", shipping_weight);
            model.addAttribute("prices", prices);
            model.addAttribute("total", price_total);
            model.addAttribute("totalAmount", shipping_weight+price_total);
            return "basket";
        }
    }
        
}

