package com.tea.application.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tea.application.entity.Basket;
import com.tea.application.repository.BasketRepository;

@RunWith(MockitoJUnitRunner.class)
public class BasketServiceTests {

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketService basketService;

    @Test
    public void testGetBasketByUser() {
        
        String userId = "userId";
        Basket expectedBasket = new Basket();
        when(basketRepository.findByUserId(userId)).thenReturn(expectedBasket);

        Basket actualBasket = basketService.getBasketByUser(userId);

        assertEquals(expectedBasket, actualBasket);
    }

    @Test
    public void testSaveBasket() {
        
        Basket basket = new Basket();

        basketService.saveBasket(basket);

        verify(basketRepository).save(basket);
    }

    @Test
    public void testDeleteBasketByUser() {
       
        String userId = "userId";

        basketService.deleteBasketByUser(userId);

        verify(basketRepository).deleteByUserId(userId);
    }
}

