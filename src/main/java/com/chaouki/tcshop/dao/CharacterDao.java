package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by chaouki on 19/03/2018.
 */
public interface CharacterDao extends JpaRepository<Character, Integer> {
    List<Character> findByAccount(Integer account);
}
