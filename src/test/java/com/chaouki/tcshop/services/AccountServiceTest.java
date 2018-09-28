package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.AccountDao;
import com.chaouki.tcshop.entities.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDao accountDao;

    @Sql("/sql/cleanAllTables.sql")
    @Test
    public void onAccountMessage() {
        Assert.assertThat(accountDao.count(), is(0L));

        final int accountId = 1;
        final LocalDateTime timestamp = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
        final String username = "USERNAME";
        final String pwd = "PWD";
        accountService.onAccountMessage(accountId, timestamp, username, pwd);

        List<Account> accountList = accountDao.findAll();
        Assert.assertThat(accountList.size(), is(1));
        Account savedAccount = accountList.get(0);
        Assert.assertThat(savedAccount.getId(), is(accountId));
        Assert.assertThat(savedAccount.getUsername(), is(username));
        Assert.assertThat(savedAccount.getHashedPassword(), is(pwd));
        Assert.assertThat(savedAccount.getLastUpdateTimestamp(), is(timestamp));
        Assert.assertThat(savedAccount.isStub(), is(false));
    }

    @Test
    public void findById() {
        Optional<Account> optionalAccount = accountService.findById(4);
        Account account = optionalAccount.orElseThrow(RuntimeException::new);

        Assert.assertThat(account.getId(), is(4));
        Assert.assertThat(account.getUsername(), is("USERNAME_4"));
        Assert.assertThat(account.getHashedPassword(), is("PWD_4"));
        final LocalDateTime timestamp = LocalDateTime.of(2000, 1, 3, 0, 0);
        Assert.assertThat(account.getLastUpdateTimestamp(), is(timestamp));
        Assert.assertThat(account.isStub(), is(false));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Test
    public void findByUsername_notStub() {
        Account account = accountService.findByUsername("USERNAME_4");

        Assert.assertThat(account.getId(), is(4));
        Assert.assertThat(account.getUsername(), is("USERNAME_4"));
        Assert.assertThat(account.getHashedPassword(), is("PWD_4"));
        final LocalDateTime timestamp = LocalDateTime.of(2000, 1, 3, 0, 0);
        Assert.assertThat(account.getLastUpdateTimestamp(), is(timestamp));
        Assert.assertThat(account.isStub(), is(false));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Test(expected = IllegalArgumentException.class)
    public void findByUsername_stub() {
        Account account = accountService.findByUsername("USERNAME_5");
    }

    @Sql("/sql/cleanAllTables.sql")
    @Test
    public void createStubAccount() {
        Assert.assertThat(accountDao.count(), is(0L));

        final int accountId = 1;
        accountService.createStubAccount(accountId);

        List<Account> accountList = accountDao.findAll();
        Assert.assertThat(accountList.size(), is(1));
        Account savedAccount = accountList.get(0);
        Assert.assertThat(savedAccount.getId(), is(accountId));
        Assert.assertThat(savedAccount.getUsername(), is(AccountServiceImpl.STUB_ACCOUNT));
        Assert.assertThat(savedAccount.getHashedPassword(), is(AccountServiceImpl.STUB_ACCOUNT));
        Assert.assertThat(savedAccount.getLastUpdateTimestamp(), is(LocalDateTime.of(2000, 1,1,0,0)));
        Assert.assertThat(savedAccount.isStub(), is(true));
    }
}