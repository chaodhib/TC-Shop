package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.ItemTemplate;
import com.chaouki.tcshop.services.AccountService;
import com.chaouki.tcshop.services.CharacterService;
import com.chaouki.tcshop.services.ItemTemplateService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class ItemController {

    @Autowired
    private ItemTemplateService itemTemplateService;

    private LazyDataModel<ItemTemplate> itemTemplates;

    @PostConstruct
    public void init(){
        itemTemplates = new ItemLazyDataModel(itemTemplateService);
    }

    public LazyDataModel<ItemTemplate> getItemTemplates() {
        return itemTemplates;
    }
}
