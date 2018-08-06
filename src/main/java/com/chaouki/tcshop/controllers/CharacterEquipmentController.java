package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterEquipment;
import com.chaouki.tcshop.services.AccountService;
import com.chaouki.tcshop.services.CharacterEquipmentService;
import com.chaouki.tcshop.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ManagedBean
@ViewScoped
public class CharacterEquipmentController {

    private static final String CHARACTER_ID_PARAM = "characterId";

    @Autowired
    private CharacterEquipmentService characterEquipmentService;

    @Autowired
    private CharacterService characterService;

    private List<CharacterEquipment> characterEquipmentList;
    private LocalDateTime lastUpdate;

    @PostConstruct
    public void init(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User)authentication.getPrincipal()).getUsername();

        if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey(CHARACTER_ID_PARAM)) {
            String characterIdString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(CHARACTER_ID_PARAM);
            Optional<Character> character = characterService.findById(Integer.valueOf(characterIdString));

            // the character exists and is owned by the currently logged user
            if(character.isPresent() && character.get().getAccount().getUsername().equals(username)) {
                lastUpdate = character.get().getEquipmentUpdateTimestamp();
                characterEquipmentList = characterEquipmentService.findByCharacter(character.get());
            } else {

            }
        }
    }

    public List<CharacterEquipment> getCharacterEquipmentList() {
        return characterEquipmentList;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
