package com.tea.application.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "items")
public class Item {
    @Id
    private String id;

    private String name; 

    private String supplier;

    private String typeName;

    private int amountInGrams;

    private double itemPriceGBP;
    
}
