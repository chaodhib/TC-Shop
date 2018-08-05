package com.chaouki.tcshop.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Account {

    @Id
    private Integer id;
    private String username;

    @Column(name = "hashed_password")
    private String hashedPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
