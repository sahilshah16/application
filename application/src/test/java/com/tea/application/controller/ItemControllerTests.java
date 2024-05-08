package com.tea.application.controller;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.tea.application.entity.Item;
import com.tea.application.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTests {
    @Mock
    private HttpServletRequest request;

    @Mock
    private ItemService itemService;

    @Mock
    private Model model;

    @InjectMocks
    private ItemController itemController;

    @Test
    public void testGetItemInfo_AuthenticatedUser_ItemExists_ReturnsItemPage() {
        
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("authenticated")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn("123");

        String itemId = "itemId";
        Item item = new Item();

        when(itemService.searchById(itemId)).thenReturn(Optional.of(item));

        String viewName = itemController.getItemInfo(itemId, model);

        assertEquals("item", viewName);

        verify(model).addAttribute("userId", "123");

        verify(model).addAttribute("item", item);

        assertNull(model.getAttribute("noItem"));

        verify(itemService).searchById(itemId);
    }

    @Test
    public void testGetItemInfo_AuthenticatedUser_ItemNotExists_ReturnsItemPageWithNoItemAttribute() {
       
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("authenticated")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn("123");

        String itemId = "nonExistingItemId";

        when(itemService.searchById(itemId)).thenReturn(Optional.empty());

        String viewName = itemController.getItemInfo(itemId, model);

        assertEquals("item", viewName);

        verify(model).addAttribute("userId", "123");

        assertNull(model.getAttribute("item"));

        verify(model).addAttribute("noItem", true);

        verify(itemService).searchById(itemId);
    }

    @Test
    public void testGetItemInfo_UnauthenticatedUser_ReturnsItemPageWithNoUserAttribute() {
       
        HttpSession session = mock(HttpSession.class);
        
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("authenticated")).thenReturn(false);

        String itemId = "itemId";

        String viewName = itemController.getItemInfo(itemId, model);

        assertEquals("item", viewName);
        assertNull(model.getAttribute("userId"));
        
    }

    @Test
    public void testGetItemInfo_NoSession_ReturnsItemPageWithNoUserAttribute() {
        
        when(request.getSession(false)).thenReturn(null);

        String itemId = "itemId";

        String viewName = itemController.getItemInfo(itemId, model);

        assertEquals("item", viewName);
        assertNull(model.getAttribute("userId"));
        
    }

}
