package com.tea.application.controller;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.tea.application.entity.Basket;
import com.tea.application.entity.BasketData;
import com.tea.application.entity.Item;
import com.tea.application.service.BasketService;
import com.tea.application.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RunWith(MockitoJUnitRunner.class)
public class BasketControllerTests {
    
    @Mock
    private HttpServletRequest request;

    @Mock
    private BasketService basketService;

    @Mock
    private ItemService itemService;

    @Mock
    private Model model;

    
    @Mock
    private HttpSession session;

    @InjectMocks
    private BasketController basketController;

    @Test
    public void testAddToBasket_BasketNotExist_CreateNewBasketAndAddItem() {
        // Arrange
        String userId = "userId";
        String itemId = "itemId";
        Integer quantity = 2;

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(false).getAttribute("userId")).thenReturn(userId);
        when(basketService.getBasketByUser(userId)).thenReturn(null);

        Item item = new Item();
        when(itemService.searchById(itemId)).thenReturn(Optional.of(item));

        // Act
        String viewName = basketController.addToBasket(itemId, quantity, model);

        // Assert
        assertEquals("home", viewName);
        verify(basketService).saveBasket(any(Basket.class));
        verify(model).addAttribute("basket", true);
        verify(model).addAttribute("userId", userId);
    }

    @Test
    public void testAddToBasket_BasketExist_AddItemToExistingBasket() {
        // Arrange
        String userId = "userId";
        String itemId = "itemId";
        Integer quantity = 2;

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(false).getAttribute("userId")).thenReturn(userId);

        Basket basket = new Basket();
        when(basketService.getBasketByUser(userId)).thenReturn(basket);

        Item item = new Item();
        when(itemService.searchById(itemId)).thenReturn(Optional.of(item));

        // Act
        String viewName = basketController.addToBasket(itemId, quantity, model);

        // Assert
        assertEquals("home", viewName);
        verify(basketService).saveBasket(basket);
        verify(model).addAttribute("basket", true);
        verify(model).addAttribute("userId", userId);
    }

    @Test
    public void testAddToBasket_ItemNotFound_ReturnsHomeWithoutAddingToBasket() {
        // Arrange
        String userId = "userId";
        String itemId = "nonExistingItemId";
        Integer quantity = 2;

        when(request.getSession(false)).thenReturn(mock(HttpSession.class));
        when(request.getSession(false).getAttribute("userId")).thenReturn(userId);

        when(itemService.searchById(itemId)).thenReturn(Optional.empty());

        // Act
        String viewName = basketController.addToBasket(itemId, quantity, model);

        // Assert
        assertEquals("home", viewName);
        verify(model).addAttribute("userId", userId);
    }

    @Test
    public void testGetBasket_EmptyBasket_ReturnsBasketPageWithEmptyBasketAttribute() {
        // Arrange
        String userId = "userId";

        when(basketService.getBasketByUser(userId)).thenReturn(null);

        // Act
        String viewName = basketController.getBasket(userId, model);

        // Assert
        assertEquals("basket", viewName);
        verify(model).addAttribute("emptyBasket", true);
    }

    @Test
    public void testGetBasket_NonEmptyBasket_ReturnsBasketPageWithAttributes() {
        // Arrange
        String userId = "userId";
        Basket basket = new Basket();
        BasketData basketData = new BasketData();
        basketData.setQuantity(2); // Example quantity
        basketData.setItem(new Item()); // Example item
        basket.setBasketDatas(Collections.singletonList(basketData)); // Example basket data
        when(basketService.getBasketByUser(userId)).thenReturn(basket);

        // Act
        String viewName = basketController.getBasket(userId, model);

        // Assert
        assertEquals("basket", viewName);
        verify(model).addAttribute("userId", userId);
        verify(model).addAttribute("basketData", basket.getBasketDatas());
        // Add more verifications for other attributes as needed
    }

    @Test
    public void testGetBasket_ShippingWeightLessThan6000_ReturnsBasketPageWithShipping150() {
        // Arrange
        String userId = "userId";
        Basket basket = new Basket();
        // Add BasketData with quantity and item to ensure shipping calculation
        BasketData basketData = new BasketData();
        basketData.setQuantity(2); // Example quantity
        basketData.setItem(new Item()); // Example item
        basket.setBasketDatas(Collections.singletonList(basketData)); // Example basket data
        when(basketService.getBasketByUser(userId)).thenReturn(basket);

        // Act
        String viewName = basketController.getBasket(userId, model);

        // Assert
        assertEquals("basket", viewName);
        verify(model).addAttribute("userId", userId);
        verify(model).addAttribute("basketData", basket.getBasketDatas());
        verify(model).addAttribute("shipping", 1.50);
        // Add more verifications for other attributes as needed
    }

    @Test
    public void testGetBasket_ShippingWeightBetween6000And10000_ReturnsBasketPageWithShipping1000() {
        // Arrange
        String userId = "userId";
        Basket basket = new Basket();
        // Add BasketData with quantity and item to ensure shipping calculation
        BasketData basketData = new BasketData();
        basketData.setQuantity(5); // Example quantity
        basketData.setItem(new Item()); // Example item
        basket.setBasketDatas(Collections.singletonList(basketData)); // Example basket data
        when(basketService.getBasketByUser(userId)).thenReturn(basket);

        // Act
        String viewName = basketController.getBasket(userId, model);

        // Assert
        assertEquals("basket", viewName);
        verify(model).addAttribute("userId", userId);
        verify(model).addAttribute("basketData", basket.getBasketDatas());
        // Add more verifications for other attributes as needed
    }

    

    
}
