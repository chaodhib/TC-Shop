package com.chaouki.tcshop.entities.enums;

public enum InventoryType {

    NON_EQUIPABLE(0, "Non equipable"),
    HEAD(1, "Head"),
    NECK(2, "Neck"),
    SHOULDER(3, "Shoulder"),
    SHIRT(4, "Shirt"),
    CHEST(5, "Chest"),
    WAIST(6, "Waist"),
    LEGS(7, "Legs"),
    FEET(8, "Feet"),
    WRISTS(9, "Wrists"),
    HANDS(10, "Hands"),
    FINGER(11, "Finger"),
    TRINKET(12, "Trinket"),
    WEAPON(13, "Weapon"),
    SHIELD(14, "Shield"),
    RANGED(15, "Ranged"),
    BACK(16, "Back"),
    TWO_HAND(17, "Two-Hand"),
    BAG(18, "Bag"),
    TABARD(19, "Tabard"),
    ROBE(20, "Robe"),
    MAIN_HAND(21, "Main hand"),
    OFF_HAND(22, "Off hand"),
    HOLDABLE(23, "Holdable"),
    AMMO(24, "Ammo"),
    THROWN(25, "Thrown"),
    RANGED_RIGHT(26, "Ranged right"),
    QUIVER(27, "Quiver"),
    RELIC(28, "Relic");

    private final int idx;
    private final String label;

    InventoryType(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public int getIdx() {
        return idx;
    }

    public String getLabel() {
        return label;
    }

    public static InventoryType getByIndex(int index){
        for (InventoryType value : InventoryType.values()) {
            if(value.getIdx() == index)
                return value;
        }
        return null;
    }
}
