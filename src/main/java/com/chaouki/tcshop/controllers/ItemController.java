package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.ItemTemplate;
import com.chaouki.tcshop.entities.enums.InventoryType;
import com.chaouki.tcshop.entities.enums.ItemClass;
import com.chaouki.tcshop.services.AccountService;
import com.chaouki.tcshop.services.CharacterService;
import com.chaouki.tcshop.services.ItemCatalogService;
import com.chaouki.tcshop.services.ItemTemplateService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class ItemController implements Serializable {

    @Autowired
    private ItemTemplateService itemTemplateService;

    @Autowired
    private ItemCatalogService itemCatalogService;

    private LazyDataModel<ItemTemplate> itemTemplates;
    private List<ItemTemplate> filteredItems;
    private List<SelectItem> itemClassOptions;
    private List<SelectItem> inventoryTypeOptions;

    @PostConstruct
    public void init(){
        itemTemplates = new ItemLazyDataModel(itemTemplateService);
        itemClassOptions = Arrays.stream(ItemClass.values()).map(itemClass -> new SelectItem(itemClass.getIdx(),itemClass.getLabel())).collect(Collectors.toList());
        itemClassOptions.add(0, new SelectItem(""));
        inventoryTypeOptions = Arrays.stream(InventoryType.values()).map(inventoryType -> new SelectItem(inventoryType.getIdx(),inventoryType.getLabel())).collect(Collectors.toList());
        inventoryTypeOptions.add(0, new SelectItem(""));
    }

    public BigDecimal getItemPricePerUnit(ItemTemplate itemTemplate)  {
        return itemCatalogService.getItemPricePerUnit(itemTemplate);
    }


    public LazyDataModel<ItemTemplate> getItemTemplates() {
        return itemTemplates;
    }

    public List<ItemTemplate> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<ItemTemplate> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<SelectItem> getItemClassOptions() {
        return itemClassOptions;
    }

    public List<SelectItem> getInventoryTypeOptions() {
        return inventoryTypeOptions;
    }
}
