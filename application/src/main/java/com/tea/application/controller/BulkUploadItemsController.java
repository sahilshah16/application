package com.tea.application.controller;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.tea.application.entity.Item;
import com.tea.application.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BulkUploadItemsController {
    
    @Autowired
    private HttpServletRequest request;

    @Autowired
    ItemService itemService;
    
    @GetMapping("/bulkAdd")
    public String adminBulkUpload(Model model){
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            
            if (session.getAttribute("adminId") != null) {
    
                String adminId= (String) session.getAttribute("adminId");

                model.addAttribute("adminId", adminId);
            }
        }
        return "adminBulkUpload";
    }

    @PostMapping("/uploadCsv")
    public String uploadCsv(@RequestParam("csvFile") MultipartFile file, Model model) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            
            if (session.getAttribute("adminId") != null) {
    
                String adminId= (String) session.getAttribute("adminId");

                model.addAttribute("adminId", adminId);
            }
        }
        
        try {
            // Create a CSV reader
            Reader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReader(reader);

            List<Item> items = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                
                String name = line[0];
                String supplier = line[1];
                String typeName = line[2];
                int amountInGrams = Integer.parseInt(line[3]);
                double itemPriceGBP = Double.parseDouble(line[4]);
                
                Item item = new Item(name, supplier, typeName, amountInGrams,itemPriceGBP);

                if(itemService.searchByName(name).size()>0){
                    csvReader.close();
                    throw new Exception();
                }
                items.add(item);
                
            }
            for(Item itemChecked: items){
                itemService.saveItem(itemChecked);
            }
            csvReader.close();
            model.addAttribute("successMessage", true);
            return "adminBulkUpload";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error parsing CSV file.");
            return "adminBulkUpload"; 
        }
        
    }
}
