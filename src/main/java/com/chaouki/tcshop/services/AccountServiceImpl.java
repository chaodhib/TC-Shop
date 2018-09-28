package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.AccountDao;
import com.chaouki.tcshop.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.util.Assert;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    public static final String STUB_ACCOUNT = "STUB@ACCOUNT";

    private AccountDao accountDao;

    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void onAccountMessage(Integer id, LocalDateTime timestamp, String username, String hashedPassword) {

        Optional<Account> savedAccount = accountDao.findById(id);
        if(savedAccount.isPresent() && savedAccount.get().getLastUpdateTimestamp().compareTo(timestamp) >= 0) // duplicate or out of order message. ignore.
            return;

        if(savedAccount.isPresent()) {
            Account account = savedAccount.get();
            account.setLastUpdateTimestamp(timestamp);
            account.setUsername(username);
            account.setHashedPassword(hashedPassword);
            account.setStub(false);
            accountDao.save(account);

        } else {
            Account account = new Account();
            account.setId(id);
            account.setLastUpdateTimestamp(timestamp);
            account.setUsername(username);
            account.setHashedPassword(hashedPassword);
            account.setStub(false);
            accountDao.save(account);
        }
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return accountDao.findById(id);
    }

    @Override
    public Account findByUsername(String username) {
        Optional<Account> optionalAccount = accountDao.findByUsername(username);
        if(!optionalAccount.isPresent() || optionalAccount.get().isStub())
            throw new IllegalArgumentException();

        return optionalAccount.get();
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
