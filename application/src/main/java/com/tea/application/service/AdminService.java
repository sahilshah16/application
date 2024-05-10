package com.tea.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tea.application.entity.Admin;
import com.tea.application.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    public Admin getAdminByUsername(String username){
        return adminRepository.findByUsername(username);
    }

    public Optional<Admin> getAdminById(String id){
        return adminRepository.findById(id);
    }

    public Admin saveAdmin(Admin admin){
        String hashedPassword = passwordEncoderService.hashPassword(admin.getPassword());
        admin.setPassword(hashedPassword);
        return adminRepository.save(admin);
    } 

    public boolean authenticate(String password, String hashedPassword){
        return passwordEncoderService.checkPassword(password, hashedPassword);
    }
}

