package com.tea.application.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String username;

    private String password;

    private LocalDateTime date;
    
    public User() {
        this.date = LocalDateTime.now();
    }
    public User(String username, String password){
        this.date = LocalDateTime.now();
        this.username=username;
        this.password=password;
    }
    public User(String id, String username, String password){
        this.date = LocalDateTime.now();
        this.id=id;
        this.username=username;
        this.password=password;
    }
}
