package com.tea.application.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
}
