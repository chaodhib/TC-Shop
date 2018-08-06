package com.chaouki.tcshop.entities.enums;

public enum ItemClass {

    CONSUMABLE(0, "Consumable"),
    CONTAINER(1, "Container"),
    WEAPON(2, "Weapon"),
    GEM(3, "Gem"),
    ARMOR(4, "Armor"),
    REAGENT(5, "Reagent"),
    PROJECTILE(6, "Projectile"),
    TRADE_GOODS(7, "Trade Goods"),
    GENERIC(8, "Generic"),
    RECIPE(9, "Recipe"),
    MONEY(10, "Money"),
    QUIVER(11, "Quiver"),
    QUEST(12, "Quest"),
    KEY(13, "Key"),
    PERMANENT(14, "Permanent"),
    MISCELLANEOUS(15, "Miscellaneous"),
    GLYPH(16, "Glyph");

    private final int idx;
    private final String label;

    ItemClass(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public int getIdx() {
        return idx;
    }

    public String getLabel() {
        return label;
    }

    public static ItemClass getByIndex(int index){
        for (ItemClass value : ItemClass.values()) {
            if(value.getIdx() == index)
                return value;
        }
        return null;
    }
}
