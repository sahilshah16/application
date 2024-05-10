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
@Document(collection = "admins")
public class Admin {
    @Id
    private String id;

    private String username;

    private String password;

    private LocalDateTime date;
    
    public Admin() {
        this.date = LocalDateTime.now();
    }
}
