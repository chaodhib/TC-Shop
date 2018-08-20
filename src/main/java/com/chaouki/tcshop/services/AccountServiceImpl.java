package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.AccountDao;
import com.chaouki.tcshop.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    public static final String STUB_ACCOUNT = "STUB@ACCOUNT";
    @Autowired
    private AccountDao accountDao;

    @Override
    public void onAccountMessage(Integer id, LocalDateTime timestamp, String username, String hashedPassword) {

        Optional<Account> savedAccount = accountDao.findById(id);
        if(savedAccount.isPresent() && savedAccount.get().getLastUpdateTimestamp().compareTo(timestamp) >= 0) // duplicate or out of order message. ignore.
            return;

        if(savedAccount.isPresent()) {
            Account account = savedAccount.get();
            account.setLastUpdateTimestamp(timestamp);
            account.setUsername(username);
            account.setHashedPassword(hashedPassword);
            accountDao.save(account);

        } else {
            Account account = new Account();
            account.setId(id);
            account.setLastUpdateTimestamp(timestamp);
            account.setUsername(username);
            account.setHashedPassword(hashedPassword);
            accountDao.save(account);
        }
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return accountDao.findById(id);
    }

    @Override
    public Account findByUsername(String username) {
        if(STUB_ACCOUNT.equals(username))
            throw new IllegalArgumentException("this username is reserved");

        return accountDao.findByUsername(username).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Account createStubAccount(Integer accountId) {
        Assert.notNull(accountId, "accountId can't be null");

        Account account = new Account();
        account.setId(accountId);
        account.setLastUpdateTimestamp(LocalDateTime.of(2000, 1,1,0,0));
        account.setUsername(STUB_ACCOUNT);
        account.setHashedPassword(STUB_ACCOUNT);
        account.setStub(true);
        return accountDao.save(account);
    }
}
