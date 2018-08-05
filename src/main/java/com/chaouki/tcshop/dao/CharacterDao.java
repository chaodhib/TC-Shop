package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterDao extends JpaRepository<Character, Integer> {
    List<Character> findByAccount(Account account);
}
