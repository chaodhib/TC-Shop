package com.chaouki.tcshop.entities;

import com.chaouki.tcshop.entities.converters.CharacterConverter;
import com.chaouki.tcshop.entities.converters.RaceConverter;

import javax.persistence.*;

@Entity
@Table(name = "character_")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer guid;

    @ManyToOne
    private Account account;

    private String name;

    @Convert(converter = CharacterConverter.class)
    @Column(name = "character_class", columnDefinition = "TINYINT")
    private CharacterClass characterClass;

    public Integer getGuid() {
        return guid;
    }

    public void setGuid(Integer guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }
}
