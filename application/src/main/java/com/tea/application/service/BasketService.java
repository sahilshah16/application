package com.tea.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tea.application.entity.Basket;
import com.tea.application.repository.BasketRepository;

@Service
public class BasketService {

    @Autowired
    BasketRepository basketRepository;

    public Basket getBasketByUser(String userId){
        return basketRepository.findByUserId(userId);
    }

    public void saveBasket(Basket basket){
        basketRepository.save(basket);
    }
    
    public void deleteBasketByUser(String userId){
        basketRepository.deleteByUserId(userId);
    }
    
    
}
