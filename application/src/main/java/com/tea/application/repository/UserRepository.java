package com.tea.application.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tea.application.entity.User;


public interface UserRepository extends MongoRepository<User,String>{
    User findByUsername(String username);
    List<User> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
}
