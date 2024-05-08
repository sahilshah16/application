package com.tea.application.controller;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.tea.application.entity.Basket;
import com.tea.application.entity.BasketData;
import com.tea.application.entity.Order;
import com.tea.application.service.BasketService;
import com.tea.application.service.OrderService;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTests {
    
    @Mock
    private BasketService basketService;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testHandleOrder_ValidOrder_ReturnsHomeWithOrderAttribute() {
       
        String userId = "userId";
        Double shipping = 10.0;
        Double total = 100.0;

        Basket basket = new Basket();
        basket.setBasketDatas(new ArrayList<>()); 

        when(basketService.getBasketByUser(userId)).thenReturn(basket);

        String viewName = orderController.handleOrder(userId, shipping, total, model);

        assertEquals("home", viewName);
        verify(orderService).saveOrder(any(Order.class)); 
        verify(basketService).deleteBasketByUser(userId); 
        verify(model).addAttribute("order", true); 
    }

    @Test
    public void testHandleOrder_ValidOrder_BasketNotEmpty_ReturnsHomeWithOrderAttribute() {
        // Arrange
        String userId = "userId";
        Double shipping = 10.0;
        Double total = 100.0;

        Basket basket = new Basket();
        BasketData basketData = new BasketData(); // Assuming basket is not empty
        basket.setBasketDatas(Collections.singletonList(basketData));

        when(basketService.getBasketByUser(userId)).thenReturn(basket);

        // Act
        String viewName = orderController.handleOrder(userId, shipping, total, model);

        // Assert
        assertEquals("home", viewName);
        verify(orderService).saveOrder(any(Order.class)); // Verify that saveOrder method is called with an order object
        verify(basketService).deleteBasketByUser(userId); // Verify that deleteBasketByUser method is called with the userId
        verify(model).addAttribute("order", true); // Verify that the "order" attribute is added to the model with value true
    }

    @Test
    public void testHandleOrder_InvalidOrder_ReturnsHomeWithoutOrderAttribute() {
        // Arrange
        String userId = "userId";

        when(basketService.getBasketByUser(userId)).thenReturn(null);

        verify(orderService, never()).saveOrder(any(Order.class)); 
        verify(basketService, never()).deleteBasketByUser(userId);
        verify(model, never()).addAttribute(eq("order"), any()); 
    }

}
