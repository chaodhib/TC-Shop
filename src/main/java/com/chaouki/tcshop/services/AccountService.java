package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Account;

public interface AccountService {

    Account createAccount(Integer id, String username, String hashedPassword);
}
