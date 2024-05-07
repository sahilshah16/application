package com.tea.application.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;

    private List<BasketData> basketDatas;
    
    private double shippingCost;

    private double itemCost;

    private LocalDateTime date;

    public Order() {
        this.date = LocalDateTime.now();
    }

    
}