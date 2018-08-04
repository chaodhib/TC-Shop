package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.CharacterClass;
import com.chaouki.tcshop.entities.Gender;
import com.chaouki.tcshop.entities.Race;
import com.chaouki.tcshop.services.CharacterService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chaouki on 19/03/2018.
 */
@ManagedBean
@ViewScoped
public class CharacterAddController implements ApplicationContextAware {

    private CharacterService characterService;
    private ApplicationContext ac;
    private String name;
    private String accountId;
    private CharacterClass characterClass;
    private Gender gender;
    private Race race;

    @PostConstruct
    public void init(){
        characterService = ac.getBean(CharacterService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    public List<CharacterClass> getAllCharacterClasses(){
        return Arrays.asList(CharacterClass.values());
    }

    public List<Gender> getAllGenders(){
        return Arrays.asList(Gender.values());
    }

    public List<Race> getAllRaces(){
        return Arrays.asList(Race.values());
    }

    public String createCharacter(){
        characterService.create(Integer.valueOf(accountId), name, race, characterClass, gender);
        return "index.xhtml";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
