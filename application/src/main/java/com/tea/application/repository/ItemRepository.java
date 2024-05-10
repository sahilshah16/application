package com.tea.application.repository;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tea.application.entity.Item;

public interface ItemRepository extends MongoRepository<Item,String>{

    @Query("{'$or': [{'name': { '$regex': '\\b'+?0+'\\b', '$options': 'i' }}, {'supplier': { '$regex': '\\b'+?0+'\\b', '$options': 'i' }}, {'typeName': { '$regex': '\\b'+?0+'\\b', '$options': 'i' }}]}")
    public HashSet<Item> searchItemsByKeyword(String searchTerm);

    List<Item> findByName(String name);

    public List<Item> findByNameIgnoreCase(String name);

    public List<Item> findBySupplierIgnoreCase(String supplier);
  
    public List<Item> findByTypeNameIgnoreCase(String typeName);
   
}
