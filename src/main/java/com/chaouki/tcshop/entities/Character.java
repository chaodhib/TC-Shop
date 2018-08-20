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

    @Column(name = "is_deleted")
    private boolean deleted;

    @Column(name = "last_update_timestamp")
    private LocalDateTime lastUpdateTimestamp;

    @Column(name = "is_stub")
    private boolean isStub;

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

    public LocalDateTime getEquipmentUpdateTimestamp() {
        return equipmentUpdateTimestamp;
    }

    public void setEquipmentUpdateTimestamp(LocalDateTime equipmentUpdateTimestamp) {
        this.equipmentUpdateTimestamp = equipmentUpdateTimestamp;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public boolean isStub() {
        return isStub;
    }

    public void setStub(boolean stub) {
        isStub = stub;
    }
}
