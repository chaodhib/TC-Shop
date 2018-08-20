package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Account;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AccountService {

    void onAccountMessage(Integer id, LocalDateTime timestamp, String username, String hashedPassword);

    Optional<Account> findById(Integer id);

    Account findByUsername(String username);

    Account createStubAccount(Integer accountId);
}
