package com.chaouki.tcshop.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CharacterEquipmentSlot implements Serializable {

    @ManyToOne
    private Character character;

    @Column(name = "slot_id")
    private Integer slotId;

    public CharacterEquipmentSlot() {
    }

    public CharacterEquipmentSlot(Character character, Integer slotId) {
        this.character = character;
        this.slotId = slotId;
    }

    public Character getCharacter() {
        return character;
    }

    public Integer getSlotId() {
        return slotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterEquipmentSlot that = (CharacterEquipmentSlot) o;
        return Objects.equals(character, that.character) &&
                Objects.equals(slotId, that.slotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, slotId);
    }
}
