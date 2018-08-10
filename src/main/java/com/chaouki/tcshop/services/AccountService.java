package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Account;

import java.util.Optional;

public interface AccountService {

    void createAccount(Integer id, String username, String hashedPassword);

    Optional<Account> findById(Integer id);

    Account findByUsername(String username);
}
