package com.tea.application.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tea.application.entity.Order;


public interface OrderRepository extends MongoRepository<Order,String>{
    Order findByUserId(String userId);
    List<Order> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
