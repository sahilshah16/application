package com.tea.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tea.application.entity.Order;


public interface OrderRepository extends MongoRepository<Order,String>{
    Order findByUserId(String userId);
}
