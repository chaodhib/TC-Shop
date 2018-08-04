package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.services.CharacterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.util.List;

/**
 * Created by chaouki on 19/03/2018.
 */
@ManagedBean
@ViewScoped
public class CharacterController implements ApplicationContextAware {

    private CharacterService characterService;

    private List<Character> characterList;
    private ApplicationContext ac;
    private String accountId;

    @PostConstruct
    public void init(){
        characterService = ac.getBean(CharacterService.class);
        characterList = characterService.findAll();
    }

    public List<Character> getCharacterList() {
        return characterList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    public void filterByAccount(){
        if(accountId != null && StringUtils.isNumeric(accountId))
            characterList = characterService.findByAccount(Integer.valueOf(accountId));
        else
            characterList = characterService.findAll();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
