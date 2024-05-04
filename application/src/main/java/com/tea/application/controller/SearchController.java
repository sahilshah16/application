package com.tea.application.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tea.application.entity.Item;
import com.tea.application.service.ItemService;

@Controller
public class SearchController {

    @Autowired
    ItemService itemService;

    @GetMapping("/search")
    public ResponseEntity<HashSet<Item>> getSearchResults(@RequestParam String searchTerm){
        String[] words = searchTerm.split("\\s+");
        Arrays.stream(words).filter(word -> !word.isEmpty()).toArray(String[]::new);
        return new ResponseEntity<>(itemService.searchArray(words), HttpStatus.OK);
    }

    
}
