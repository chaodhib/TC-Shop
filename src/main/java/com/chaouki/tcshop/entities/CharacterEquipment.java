package com.chaouki.tcshop.entities;

import javax.persistence.*;

@Entity
@Table(name = "character_equipment")
public class CharacterEquipment {

    @EmbeddedId
    private CharacterEquipmentSlot slot;

    @ManyToOne
    @JoinColumn(name = "item_template_id")
    private ItemTemplate itemTemplate;

    public CharacterEquipmentSlot getSlot() {
        return slot;
    }

    public void setSlot(CharacterEquipmentSlot slot) {
        this.slot = slot;
    }

    public ItemTemplate getItemTemplate() {
        return itemTemplate;
    }

    public void setItemTemplate(ItemTemplate itemTemplate) {
        this.itemTemplate = itemTemplate;
    }
}
