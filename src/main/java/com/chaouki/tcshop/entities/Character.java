package com.chaouki.tcshop.entities;

import com.chaouki.tcshop.entities.converters.CharacterConverter;
import com.chaouki.tcshop.entities.converters.RaceConverter;

import javax.persistence.*;

/**
 * Created by chaouki on 19/03/2018.
 */
@Entity
@Table(name = "characters")
public class Character {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer guid;
    private Integer account;
    private String name;

    @Column(columnDefinition = "TINYINT")
    @Convert(converter = RaceConverter.class)
    private Race race;

    @Convert(converter = CharacterConverter.class)
    @Column(name = "class", columnDefinition = "TINYINT")
    private CharacterClass characterClass;

    @Column(columnDefinition = "TINYINT")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(columnDefinition="TEXT")
    private String taximask = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";//"100663296 0 0 8 0 0 0 0 0 0 0 0 0 0 ";

    @Column(name="position_x")
    private Float positionX;

    @Column(name="position_y")
    private Float positionY;

    @Column(name="position_z")
    private Float positionZ;

    @Column(columnDefinition = "TINYINT")
    private Boolean cinematic;

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

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Float getPositionZ() {
        return positionZ;
    }

    public void setPositionZ(Float positionZ) {
        this.positionZ = positionZ;
    }

    public Float getPositionX() {
        return positionX;
    }

    public void setPositionX(Float positionX) {
        this.positionX = positionX;
    }

    public Float getPositionY() {
        return positionY;
    }

    public void setPositionY(Float positionY) {
        this.positionY = positionY;
    }

    public Boolean getCinematic() {
        return cinematic;
    }

    public void setCinematic(Boolean cinematic) {
        this.cinematic = cinematic;
    }
}
