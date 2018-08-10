package com.chaouki.tcshop.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class AccountController implements Serializable {

    private String username;

    @PostConstruct
    public void setUp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        username = ((User)authentication.getPrincipal()).getUsername();
    }

    public String getUsername() {
        return username;
    }
}
