package com.chaouki.tcshop.entities;

import com.chaouki.tcshop.entities.converters.*;

import javax.persistence.*;

@Entity
@Table(name = "item_template")
public class ItemTemplate {

    @Id
    private Integer entry;

    @Convert(converter = ItemClassConverter.class)
    @Column(name = "class", columnDefinition = "TINYINT")
    private ItemClass itemClass;

    @Column(name = "subclass")
    private ItemSubClass itemSubClass;

    private String name;

    @Convert(converter = ItemQualityConverter.class)
    @Column(name = "Quality", columnDefinition = "TINYINT")
    private ItemQuality itemQuality;

    @Convert(converter = InventoryTypeConverter.class)
    @Column(name = "InventoryType", columnDefinition = "TINYINT")
    private InventoryType inventoryType;

    @Column(name = "ItemLevel")
    private Integer itemLevel;

    @Column(name = "RequiredLevel")
    private Integer requiredLevel;

    @Column(name = "stackable")
    private Integer countPerStackMax;

    public Integer getEntry() {
        return entry;
    }

    public void setEntry(Integer entry) {
        this.entry = entry;
    }

    public ItemClass getItemClass() {
        return itemClass;
    }

    public void setItemClass(ItemClass itemClass) {
        this.itemClass = itemClass;
    }

    public ItemSubClass getItemSubClass() {
        return itemSubClass;
    }

    public void setItemSubClass(ItemSubClass itemSubClass) {
        this.itemSubClass = itemSubClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemQuality getItemQuality() {
        return itemQuality;
    }

    public void setItemQuality(ItemQuality itemQuality) {
        this.itemQuality = itemQuality;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Integer getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(Integer itemLevel) {
        this.itemLevel = itemLevel;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Integer getCountPerStackMax() {
        return countPerStackMax;
    }

    public void setCountPerStackMax(Integer countPerStackMax) {
        this.countPerStackMax = countPerStackMax;
    }
}
