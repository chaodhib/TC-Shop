package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.services.AccountService;
import com.chaouki.tcshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class OrderHistoryController implements Serializable {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    private List<Order> orders;

    @PostConstruct
    public void init() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User)authentication.getPrincipal()).getUsername();
        Account account = accountService.findByUsername(username);
        orders = orderService.findByAccount(account);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
