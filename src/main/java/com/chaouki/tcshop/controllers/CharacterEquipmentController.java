package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterEquipment;
import com.chaouki.tcshop.services.CharacterEquipmentService;
import com.chaouki.tcshop.services.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ManagedBean
@ViewScoped
public class CharacterEquipmentController implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterEquipmentController.class);

    private static final String CHARACTER_ID_PARAM = "characterId";

    @Autowired
    private CharacterEquipmentService characterEquipmentService;

    @Autowired
    private CharacterService characterService;

    private List<CharacterEquipment> characterEquipmentList;
    private LocalDateTime lastUpdate;
    private Character character;

    @PostConstruct
    public void init(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User)authentication.getPrincipal()).getUsername();

        if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey(CHARACTER_ID_PARAM)) {
            String characterIdString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(CHARACTER_ID_PARAM);
            Optional<Character> optionalCharacter = characterService.findById(Integer.valueOf(characterIdString));

            // the character exists and is owned by the currently logged user
            if(optionalCharacter.isPresent() && optionalCharacter.get().getAccount().getUsername().equals(username)) {
                character = optionalCharacter.get();
                lastUpdate = character.getEquipmentUpdateTimestamp();
                characterEquipmentList = characterEquipmentService.findByCharacter(character);
            } else {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpStatus.NOT_FOUND.value(), "Either the resource does not exist or you are unauthorized to access it");
                    return;
                } catch (IOException e) {
                    LOGGER.error("exception thrown when redirecting user to error message", e);
                }
            }
        }
    }

    public List<CharacterEquipment> getCharacterEquipmentList() {
        return characterEquipmentList;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Character getCharacter() {
        return character;
    }
}
