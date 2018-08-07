package com.chaouki.tcshop.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AccountController {

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
