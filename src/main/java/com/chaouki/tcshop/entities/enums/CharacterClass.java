package com.chaouki.tcshop.entities.enums;

/**
 * Created by chaouki on 20/03/2018.
 */
public enum CharacterClass {

    CLASS_WARRIOR       (1, "Warrior"),
    CLASS_PALADIN       (2, "Paladin"),
    CLASS_HUNTER        (3, "Hunter"),
    CLASS_ROGUE         (4, "Rogue"),
    CLASS_PRIEST        (5, "Priest"),
    CLASS_DEATH_KNIGHT  (6, "Death Knight"),
    CLASS_SHAMAN        (7, "Shaman"),
    CLASS_MAGE          (8, "Mage"),
    CLASS_WARLOCK       (9, "Warlock"),
    CLASS_DRUID         (11, "Druid");

    private final int idx;
    private final String label;

    CharacterClass(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public int getIdx() {
        return idx;
    }

    public String getLabel() {
        return label;
    }

    public static CharacterClass getByIndex(int index){
        for (CharacterClass characterClass : CharacterClass.values()) {
            if(characterClass.getIdx() == index)
                return characterClass;
        }
        return null;
    }
}
