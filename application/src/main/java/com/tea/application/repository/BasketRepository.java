package com.tea.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tea.application.entity.Basket;

public interface BasketRepository extends MongoRepository<Basket,String>{
    Basket findByUserId(String userId);
    void deleteByUserId(String userId);
}
    

