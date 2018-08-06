package com.chaouki.tcshop.entities;

import com.chaouki.tcshop.entities.converters.CharacterConverter;
import com.chaouki.tcshop.entities.enums.CharacterClass;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "character_")
public class Character {

    @Id
    private Integer id;

    @ManyToOne
    private Account account;

    private String name;

    @Convert(converter = CharacterConverter.class)
    @Column(name = "character_class", columnDefinition = "TINYINT")
    private CharacterClass characterClass;

    @Column(name = "equipment_update_timestamp")
    private LocalDateTime equipmentUpdateTimestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
