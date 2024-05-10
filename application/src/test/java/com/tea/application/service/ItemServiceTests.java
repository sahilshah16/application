package com.tea.application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tea.application.entity.Item;
import com.tea.application.repository.ItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTests {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testSearchById_ItemFound_ReturnsItem() {
        // Arrange
        String itemId = "itemId";
        Item expectedItem = new Item();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        // Act
        Optional<Item> actualItem = itemService.searchById(itemId);

        // Assert
        assertTrue(actualItem.isPresent());
        assertEquals(expectedItem, actualItem.get());
    }

    @Test
    public void testSearchById_ItemNotFound_ReturnsEmptyOptional() {
        // Arrange
        String itemId = "nonExistingItemId";
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act
        Optional<Item> actualItem = itemService.searchById(itemId);

        // Assert
        assertFalse(actualItem.isPresent());
    }

    @Test
    public void testSearchItemsResult() {
        // Arrange
        String searchTerm = "searchTerm";
        HashSet<Item> expectedItems = new HashSet<>();
        // Add expected behavior for itemRepository.searchItemsByKeyword(searchTerm)
        when(itemRepository.searchItemsByKeyword(searchTerm)).thenReturn(expectedItems);

        // Act
        HashSet<Item> actualItems = itemService.searchItemsResult(searchTerm);

        // Assert
        assertEquals(expectedItems, actualItems);
    }

    @Test
    public void testSearchByName() {
        // Arrange
        String name = "itemName";
        List<Item> expectedItems = new ArrayList<>();
        // Add expected behavior for itemRepository.findByNameIgnoreCase(name)
        when(itemRepository.findByNameIgnoreCase(name)).thenReturn(expectedItems);

        // Act
        List<Item> actualItems = itemService.searchByName(name);

        // Assert
        assertEquals(expectedItems, actualItems);
    }

    @Test
    public void testSearchBySupplier() {
        // Arrange
        String supplier = "itemSupplier";
        List<Item> expectedItems = new ArrayList<>();
        // Add expected behavior for itemRepository.findBySupplierIgnoreCase(supplier)
        when(itemRepository.findBySupplierIgnoreCase(supplier)).thenReturn(expectedItems);

        // Act
        List<Item> actualItems = itemService.searchBySupplier(supplier);

        // Assert
        assertEquals(expectedItems, actualItems);
    }

    @Test
    public void testSearchByTypeName() {
        // Arrange
        String typeName = "itemTypeName";
        List<Item> expectedItems = new ArrayList<>();
        // Add expected behavior for itemRepository.findByTypeNameIgnoreCase(typeName)
        when(itemRepository.findByTypeNameIgnoreCase(typeName)).thenReturn(expectedItems);

        // Act
        List<Item> actualItems = itemService.searchByTypeName(typeName);

        // Assert
        assertEquals(expectedItems, actualItems);
    }
}

