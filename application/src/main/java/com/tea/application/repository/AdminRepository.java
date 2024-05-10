package com.tea.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tea.application.entity.Admin;

public interface AdminRepository extends MongoRepository<Admin,String>{
    Admin findByUsername(String username);
    
}
