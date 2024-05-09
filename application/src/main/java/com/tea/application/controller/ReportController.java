package com.tea.application.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

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


        StringWriter csvWriter = new StringWriter();

        CSVWriter writer = new CSVWriter(csvWriter);

        try{

            writer.writeNext(new String[]{"Order ID", "Customer ID", "Total Price Item", "Shipping Cost", "Total Amount", "Date Orderd"}); 

            for (Order order : orders) {

                String total = Double.toString(order.getItemCost()+order.getShippingCost());

                writer.writeNext(new String[]{order.getId(), order.getUserId(), Double.toString(order.getItemCost()),Double.toString(order.getShippingCost()), total, order.getDate().toString() });

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
 
