package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountDao extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
}
