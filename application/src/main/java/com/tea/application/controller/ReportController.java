package com.tea.application.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.opencsv.CSVWriter;
import com.tea.application.entity.BasketData;
import com.tea.application.entity.Item;
import com.tea.application.entity.Order;
import com.tea.application.entity.User;
import com.tea.application.service.OrderService;
import com.tea.application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReportController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/reports")
    public String getReports(Model model){

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            
            if (session.getAttribute("adminId") != null) {
    
                String adminId= (String) session.getAttribute("adminId");

                model.addAttribute("adminId", adminId);
            }
        }
        return "adminReports";
    }

    @GetMapping("/reports/orders")
    public ResponseEntity<String> generateOrderReport(@RequestParam LocalDate startDateOrder, @RequestParam LocalDate endDateOrder, Model model) throws IOException {

        if(startDateOrder.isAfter(endDateOrder)){

           model.addAttribute("errorMessage", "Start Date Must Be Before End Date");

        }
        if(startDateOrder.isAfter(LocalDate.now())|| endDateOrder.isAfter(LocalDate.now())){

            model.addAttribute("errorMessage", "Dates Must Be Not Be In The Future");

        }

        List<Order> orders = orderService.getOrdersWithinDateRange(startDateOrder, endDateOrder);

        HashMap<Item, Integer> itemInformationMap = new HashMap<>();
        for(Order order: orders){
            for(BasketData basketData: order.getBasketDatas()){
                if(!itemInformationMap.containsKey(basketData.getItem())){
                    itemInformationMap.put(basketData.getItem(), basketData.getQuantity());
                }
                else{
                    itemInformationMap.put(basketData.getItem(), itemInformationMap.get(basketData.getItem())+basketData.getQuantity());
                }
            }
        }

        StringWriter csvWriter = new StringWriter();

        CSVWriter writer = new CSVWriter(csvWriter);

        try{

            writer.writeNext(new String[]{"Name of Item", "Supplier", "Number Of Items Sold", "Individual Item Price", "Total Amount Sold"}); 

            for (Map.Entry<Item, Integer> entry : itemInformationMap.entrySet()){

                Item item = entry.getKey();
                Integer quantity = entry.getValue();
                Double totalAmount= item.getItemPriceGBP()*quantity;

                writer.writeNext(new String[]{item.getName(), item.getSupplier(), String.valueOf(quantity),Double.toString(item.getItemPriceGBP()), Double.toString(totalAmount)});

            }
            
        }
        finally{

            writer.close();

        }

        String filename = "orders_" + startDateOrder+"_"+endDateOrder+ ".csv";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentDisposition(ContentDisposition.attachment()
            .filename(filename)
            .build());
        headers.setContentType(MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(csvWriter.toString(), headers, HttpStatus.OK);
    }

    @GetMapping("/reports/users")
    public ResponseEntity<String> generateUserReport(@RequestParam LocalDate startDateUser, @RequestParam LocalDate endDateUser, Model model) throws IOException {

        if(startDateUser.isAfter(endDateUser)){

           model.addAttribute("errorMessage", "Start Date Must Be Before End Date");

        }
        if(startDateUser.isAfter(LocalDate.now())|| endDateUser.isAfter(LocalDate.now())){

            model.addAttribute("errorMessage", "Dates Must Be Not Be In The Future");

        }

        List<User> users = userService.getOrdersWithinDateRange(startDateUser, endDateUser);


        StringWriter csvWriter = new StringWriter();

        CSVWriter writer = new CSVWriter(csvWriter);

        try{

            writer.writeNext(new String[]{"User ID", "Username Email", "Registration Date"}); 

            for (User user : users) {

                writer.writeNext(new String[]{user.getId(), user.getUsername(), user.getDate().toString()});

            }
            
        }
        finally{

            writer.close();

        }

        String filename = "users_" + startDateUser+"_"+endDateUser+ ".csv";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentDisposition(ContentDisposition.attachment()
            .filename(filename)
            .build());
        headers.setContentType(MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(csvWriter.toString(), headers, HttpStatus.OK);
    }


}
 
