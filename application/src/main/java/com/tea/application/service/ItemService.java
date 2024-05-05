package com.tea.application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tea.application.entity.Item;
import com.tea.application.repository.ItemRepository;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public Optional<Item> searchById(String id){
        return itemRepository.findById(id);
    }

    public HashSet<Item> searchItemsResult(String searchTerm){
        System.out.println("Here");
        return itemRepository.searchItemsByKeyword(searchTerm);
    }

    public List<Item> searchByName(String name){
        return itemRepository.findByNameIgnoreCase(name);
    }

    public List<Item> searchBySupplier(String supplier){
        return itemRepository.findBySupplierIgnoreCase(supplier);
    }

    public List<Item> searchByTypeName(String typeName){
        return itemRepository.findByTypeNameIgnoreCase(typeName);
    }

    public HashSet<Item> searchArray(String[] arr){
        HashSet<Item> items= new HashSet<>();
        for(String str: arr){
            for(Item nameItem: searchByName(str)){
                items.add(nameItem);
            }
            for(Item supplierItem: searchBySupplier(str)){
                items.add(supplierItem);
            }
            for(Item typesItem: searchByTypeName(str)){
                items.add(typesItem);
            }
        }
        return items;
    }

}
