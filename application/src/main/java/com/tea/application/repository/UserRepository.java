package com.tea.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tea.application.entity.User;


public interface UserRepository extends MongoRepository<User,String>{
    User findByUsername(String username);
    
}
