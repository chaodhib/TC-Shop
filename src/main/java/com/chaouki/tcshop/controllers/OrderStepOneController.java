package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.services.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class OrderStepOneController implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStepOneController.class);

    @Autowired
    private CharacterService characterService;

    private Integer characterId;
    private Character character;

    public String proceedToPayment() {
        if(characterId != null) {
            character = characterService.findById(characterId).orElseThrow(IllegalArgumentException::new);
        } else {
            throw new IllegalArgumentException();
        }

        return "/payment.xhtml";
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
