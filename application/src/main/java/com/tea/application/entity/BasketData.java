package com.tea.application.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasketData {
    private Item item;
    private int quantity; 
}
