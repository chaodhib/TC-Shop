package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.AccountDao;
import com.chaouki.tcshop.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public Account createAccount(Integer id, String username, String hashedPassword) {
        Account account = new Account();
        account.setId(id);
        account.setUsername(username);
        account.setHashedPassword(hashedPassword);
        return accountDao.save(account);
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return accountDao.findById(id);
    }

    @Override
    public Account findByUsername(String username) {
        return accountDao.findByUsername(username).orElseThrow(IllegalArgumentException::new);
    }
}
