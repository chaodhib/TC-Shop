package com.chaouki.tcshop.entities;

import com.chaouki.tcshop.entities.converters.*;
import com.chaouki.tcshop.entities.enums.InventoryType;
import com.chaouki.tcshop.entities.enums.ItemClass;
import com.chaouki.tcshop.entities.enums.ItemQuality;
import com.chaouki.tcshop.entities.enums.ItemSubClass;

import javax.persistence.*;

@Entity
@Table(name = "item_template")
public class ItemTemplate {

    @Id
    private Integer entry;

    @Convert(converter = ItemClassConverter.class)
    @Column(name = "class", columnDefinition = "TINYINT")
    private ItemClass itemClass;

    @Column(name = "subclass", columnDefinition = "TINYINT")
    private Integer itemSubClassIdx;

    private String name;

    @Column(name = "displayid")
    private Integer displayId;

    @Convert(converter = ItemQualityConverter.class)
    @Column(name = "Quality", columnDefinition = "TINYINT")
    private ItemQuality itemQuality;

    @Convert(converter = InventoryTypeConverter.class)
    @Column(name = "InventoryType", columnDefinition = "TINYINT")
    private InventoryType inventoryType;

    @Column(name = "ItemLevel", columnDefinition = "SMALLINT")
    private Integer itemLevel;

    @Column(name = "RequiredLevel", columnDefinition = "TINYINT")
    private Integer requiredLevel;

    @Column(name = "stackable")
    private Integer countPerStackMax;

    public Integer getEntry() {
        return entry;
    }

    public ItemClass getItemClass() {
        return itemClass;
    }

    public ItemSubClass getItemSubClass() {
        return ItemSubClass.getByIndexes(itemClass.getIdx(), itemSubClassIdx);
    }

    public String getName() {
        return name;
    }

    public ItemQuality getItemQuality() {
        return itemQuality;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public Integer getItemLevel() {
        return itemLevel;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public Integer getCountPerStackMax() {
        return countPerStackMax;
    }

    public Integer getDisplayId() {
        return displayId;
    }
}
