package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CharacterDao extends JpaRepository<Character, Integer> {

    @Query("SELECT charact FROM Character charact WHERE charact.account = ?1 AND charact.deleted = 0")
    List<Character> findActiveCharsByAccount(Account account);

    @Modifying
    @Query("UPDATE Character char SET equipmentUpdateTimestamp = ?2 WHERE char = ?1")
    void setEquipmentUpdateTimestamp(Character character, LocalDateTime timestamp);
}
