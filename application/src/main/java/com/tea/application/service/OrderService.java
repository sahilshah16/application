package com.tea.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tea.application.entity.Order;
import com.tea.application.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Order getOrderByUser(String userId){
        return orderRepository.findByUserId(userId);
    }

    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    public List<Order> getOrdersWithinDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByDateBetween(startDate, endDate);
    }


}
