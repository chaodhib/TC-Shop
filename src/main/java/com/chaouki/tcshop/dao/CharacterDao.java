package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CharacterDao extends JpaRepository<Character, Integer> {
    List<Character> findByAccount(Account account);

    @Modifying
    @Query("UPDATE Character char SET equipmentUpdateTimestamp = ?2 WHERE char = ?1")
    void setEquipmentUpdateTimestamp(Character character, LocalDateTime timestamp);
}
