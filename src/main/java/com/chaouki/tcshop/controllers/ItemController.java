package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.PurchasableItem;
import com.chaouki.tcshop.entities.PurchasableItem;
import com.chaouki.tcshop.entities.enums.InventoryType;
import com.chaouki.tcshop.entities.enums.ItemClass;
import com.chaouki.tcshop.services.PurchasableItemService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class ItemController implements Serializable {

    @Autowired
    private PurchasableItemService purchasableItemService;

    private LazyDataModel<PurchasableItem> itemList;
    private List<PurchasableItem> filteredItems;
    private List<SelectItem> itemClassOptions;
    private List<SelectItem> inventoryTypeOptions;

    @PostConstruct
    public void init(){
        itemList = new ItemLazyDataModel(purchasableItemService);
        itemClassOptions = Arrays.stream(ItemClass.values()).map(itemClass -> new SelectItem(itemClass.getIdx(),itemClass.getLabel())).collect(Collectors.toList());
        itemClassOptions.add(0, new SelectItem(""));
        inventoryTypeOptions = Arrays.stream(InventoryType.values()).map(inventoryType -> new SelectItem(inventoryType.getIdx(),inventoryType.getLabel())).collect(Collectors.toList());
        inventoryTypeOptions.add(0, new SelectItem(""));
    }

    public LazyDataModel<PurchasableItem> getItemList() {
        return itemList;
    }

    public List<PurchasableItem> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<PurchasableItem> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<SelectItem> getItemClassOptions() {
        return itemClassOptions;
    }

    public List<SelectItem> getInventoryTypeOptions() {
        return inventoryTypeOptions;
    }
}
