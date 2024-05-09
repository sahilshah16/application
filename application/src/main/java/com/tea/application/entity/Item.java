package com.tea.application.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document(collection = "items")
public class Item {
    @Id
    private String id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Supplier cannot be empty")
    private String supplier;

    @NotBlank(message = "Type cannot be empty")
    private String typeName;

    @Digits(integer = 6, fraction = 0, message = "Amount in Grams must be a number")
    @PositiveOrZero(message = "Amount in grams should be a positive number")
    private int amountInGrams;

    @NotNull(message = "Price of Item (GBP) cannot be empty")
    @DecimalMin(value = "0.01", message = "Price of Item (GBP) must be greater than or equal to 0.01")
    private double itemPriceGBP;
    
}
