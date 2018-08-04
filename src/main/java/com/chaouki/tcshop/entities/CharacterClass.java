package com.chaouki.tcshop.entities;

/**
 * Created by chaouki on 20/03/2018.
 */
public enum CharacterClass {

    CLASS_WARRIOR       (1),
    CLASS_PALADIN       (2),
    CLASS_HUNTER        (3),
    CLASS_ROGUE         (4),
    CLASS_PRIEST        (5),
    CLASS_DEATH_KNIGHT  (6),
    CLASS_SHAMAN        (7),
    CLASS_MAGE          (8),
    CLASS_WARLOCK       (9),
    CLASS_DRUID         (11);

    private final int idx;

    CharacterClass(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public static CharacterClass getByIndex(int index){
        for (CharacterClass characterClass : CharacterClass.values()) {
            if(characterClass.getIdx() == index)
                return characterClass;
        }
        return null;
    }
}
