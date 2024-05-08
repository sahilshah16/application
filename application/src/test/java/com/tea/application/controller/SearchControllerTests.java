package com.tea.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.tea.application.entity.Basket;
import com.tea.application.entity.Item;
import com.tea.application.service.BasketService;
import com.tea.application.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTests {

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ItemService itemService;

    @Mock
    private BasketService basketService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    public void resetMocks() {
        Mockito.reset(model, request, itemService, basketService, session);
    }
    
    @Test
    public void testGetSearchResults_AuthenticatedUser_ReturnsHome() {
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("authenticated")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn("123");

        String searchTerm = "test search term";
        String[] searchWords = searchTerm.split("\\s+");
        HashSet<Item> searchResults = new HashSet<>(Arrays.asList(new Item(), new Item()));

        when(itemService.searchArray(searchWords)).thenReturn(searchResults);

        String viewName = searchController.getSearchResults(searchTerm, model);

        assertEquals("home", viewName);
        verify(model).addAttribute("userId", "123");
        verify(model).addAttribute("searchResults", searchResults);
        verify(model).addAttribute("afterSearch", true);
       
        verify(session, times(2)).getAttribute("authenticated"); 
    }

    @Test
    public void testGetSearchResults_UnauthenticatedUser_ReturnsHome() {
        
        when(request.getSession(false)).thenReturn(null);

        String searchTerm = "test search term";

        String viewName = searchController.getSearchResults(searchTerm, model);

        assertEquals("home", viewName);
        assertNull(model.getAttribute("userId"));
        assertNull(model.getAttribute("searchResults"));
        assertNull(model.getAttribute("afterSearch"));
    }

    @Test
    public void testGetHome_AuthenticatedUser_ReturnsHome() {

        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("authenticated")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn("123");

        String viewName = searchController.getHome(model);

        assertEquals("home", viewName);
        verify(model).addAttribute("userId", "123");
        verify(model).addAttribute("afterSearch", false);
        
        
        verify(session, times(2)).getAttribute("authenticated"); 
    }

    @Test
    public void testGetHome_UnauthenticatedUser_RedirectsToLogin() {
        
        when(request.getSession(false)).thenReturn(null);

        String viewName = searchController.getHome(model);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testGetHome_NullUserId_RedirectsToLogin() {
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("authenticated")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = searchController.getHome(model);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testLogout_SessionExistsAndBasketExists_DeletesBasketAndInvalidatesSession() {
        
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("123");
        when(basketService.getBasketByUser("123")).thenReturn(new Basket());

        String viewName = searchController.logout();

        verify(basketService).deleteBasketByUser("123");
        verify(session).invalidate();
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testLogout_SessionExistsButNoBasket_DeletesSession() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("123");
        when(basketService.getBasketByUser("123")).thenReturn(null);

        // Act
        String viewName = searchController.logout();

        // Assert
        verify(basketService, never()).deleteBasketByUser(anyString());
        verify(session).invalidate();
        assertEquals("redirect:/login", viewName);
    }

    

}
