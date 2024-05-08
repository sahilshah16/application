package com.tea.application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tea.application.entity.Order;
import com.tea.application.repository.OrderRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testGetOrderByUser_OrderFound_ReturnsOrder() {
        // Arrange
        String userId = "userId";
        Order expectedOrder = new Order();
        when(orderRepository.findByUserId(userId)).thenReturn(expectedOrder);

        // Act
        Order actualOrder = orderService.getOrderByUser(userId);

        // Assert
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetOrderByUser_OrderNotFound_ReturnsNull() {
        // Arrange
        String userId = "nonExistingUserId";
        when(orderRepository.findByUserId(userId)).thenReturn(null);

        // Act
        Order actualOrder = orderService.getOrderByUser(userId);

        // Assert
        assertNull(actualOrder);
    }

    @Test
    public void testSaveOrder() {
        // Arrange
        Order order = new Order();

        // Act
        orderService.saveOrder(order);

        // Assert
        verify(orderRepository).save(order);
    }
}

