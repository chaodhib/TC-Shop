package com.chaouki.tcshop.entities.enums;

public enum EquipmentSlot {

    EQUIPMENT_SLOT_HEAD(0, "Head"),
    EQUIPMENT_SLOT_NECK(1, "Neck"),
    EQUIPMENT_SLOT_SHOULDERS(2, "Shoulders"),
    EQUIPMENT_SLOT_BODY(3, "Body"),
    EQUIPMENT_SLOT_CHEST(4, "Chest"),
    EQUIPMENT_SLOT_WAIST(5, "Waist"),
    EQUIPMENT_SLOT_LEGS(6, "Legs"),
    EQUIPMENT_SLOT_FEET(7, "Feet"),
    EQUIPMENT_SLOT_WRISTS(8, "Wrists"),
    EQUIPMENT_SLOT_HANDS(9, "Hands"),
    EQUIPMENT_SLOT_FINGER1(10, "Finger1"),
    EQUIPMENT_SLOT_FINGER2(11, "Finger2"),
    EQUIPMENT_SLOT_TRINKET1(12, "Trinket1"),
    EQUIPMENT_SLOT_TRINKET2(13, "Trinket2"),
    EQUIPMENT_SLOT_BACK(14, "Back"),
    EQUIPMENT_SLOT_MAINHAND(15, "Mainhand"),
    EQUIPMENT_SLOT_OFFHAND(16, "Offhand"),
    EQUIPMENT_SLOT_RANGED(17, "Ranged"),
    EQUIPMENT_SLOT_TABARD(18, "Tabard");

    private final int idx;
    private final String label;

    EquipmentSlot(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public static EquipmentSlot getByIndex(int index) {
        for (EquipmentSlot value : EquipmentSlot.values()) {
            if (value.getIdx() == index)
                return value;
        }
        return null;
    }

    public int getIdx() {
        return idx;
    }

    public String getLabel() {
        return label;
    }
}