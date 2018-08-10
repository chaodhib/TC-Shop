package com.chaouki.tcshop.entities;

import com.chaouki.tcshop.entities.converters.EquipmentSlotConverter;
import com.chaouki.tcshop.entities.enums.EquipmentSlot;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CharacterEquipmentSlot implements Serializable {

    @ManyToOne
    private Character character;

    @Convert(converter = EquipmentSlotConverter.class)
    @Column(name = "slot_id")
    private EquipmentSlot equipmentSlot;

    // For hibernate only
    public CharacterEquipmentSlot() {
    }

    public CharacterEquipmentSlot(Character character, EquipmentSlot equipmentSlot) {
        this.character = character;
        this.equipmentSlot = equipmentSlot;
    }

    public Character getCharacter() {
        return character;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterEquipmentSlot that = (CharacterEquipmentSlot) o;
        return Objects.equals(character, that.character) &&
                equipmentSlot == that.equipmentSlot;
    }

    @Override
    public int hashCode() {

        return Objects.hash(character, equipmentSlot);
    }
}
