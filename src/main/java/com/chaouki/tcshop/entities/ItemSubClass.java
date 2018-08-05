package com.chaouki.tcshop.entities;

public enum ItemSubClass {

    CONSUMABLE(0, 0, "Consumable"),
    POTION(0, 1, "Potion"),
    ELIXIR(0, 2, "Elixir"),
    FLASK(0, 3, "Flask"),
    SCROLL(0, 4, "Scroll"),
    FOOD_AND_DRINK(0, 5, "Food & Drink"),
    ITEM_ENHANCEMENT(0, 6, "Item Enhancement"),
    BANDAGE(0, 7, "Bandage"),
    OTHER_CONSUMABLE(0, 8, "Other"),
    BAG(1, 0, "Bag"),
    SOUL_BAG(1, 1, "Soul Bag"),
    HERB_BAG(1, 2, "Herb Bag"),
    ENCHANTING_BAG(1, 3, "Enchanting Bag"),
    ENGINEERING_BAG(1, 4, "Engineering Bag"),
    GEM_BAG(1, 5, "Gem Bag"),
    MINING_BAG(1, 6, "Mining Bag"),
    LEATHERWORKING_BAG(1, 7, "Leatherworking Bag"),
    INSCRIPTION_BAG(1, 8, "Inscription Bag"),
    AXE_ONE_HANDED(2, 0, "Axe One handed"),
    AXE_TWO_HANDED(2, 1, "Axe Two handed"),
    BOW(2, 2, "Bow"),
    GUN(2, 3, "Gun"),
    MACE_ONE_HANDED(2, 4, "Mace One handed"),
    MACE_TWO_HANDED(2, 5, "Mace Two handed"),
    POLEARM(2, 6, "Polearm"),
    SWORD_ONE_HANDED(2, 7, "Sword One handed"),
    SWORD_TWO_HANDED(2, 8, "Sword Two handed"),
    OBSOLETE(2, 9, "Obsolete"),
    STAFF(2, 10, "Staff"),
    EXOTIC_A(2, 11, "Exotic"),
    EXOTIC_B(2, 12, "Exotic"),
    FIST_WEAPON(2, 13, "Fist Weapon"),
    MISCELLANEOUS_WEAPON(2, 14, "Miscellaneous"),
    DAGGER(2, 15, "Dagger"),
    THROWN(2, 16, "Thrown"),
    SPEAR(2, 17, "Spear"),
    CROSSBOW(2, 18, "Crossbow"),
    WAND(2, 19, "Wand"),
    FISHING_POLE(2, 20, "Fishing Pole"),
    RED(3, 0, "Red"),
    BLUE(3, 1, "Blue"),
    YELLOW(3, 2, "Yellow"),
    PURPLE(3, 3, "Purple"),
    GREEN(3, 4, "Green"),
    ORANGE(3, 5, "Orange"),
    META(3, 6, "Meta"),
    SIMPLE(3, 7, "Simple"),
    PRISMATIC(3, 8, "Prismatic"),
    MISCELLANEOUS_ARMOR(4, 0, "Miscellaneous"),
    CLOTH_ARMOR(4, 1, "Cloth"),
    LEATHER_ARMOR(4, 2, "Leather"),
    MAIL(4, 3, "Mail"),
    PLATE(4, 4, "Plate"),
    SHIELD(4, 6, "Shield"),
    LIBRAM(4, 7, "Libram"),
    IDOL(4, 8, "Idol"),
    TOTEM(4, 9, "Totem"),
    SIGIL(4, 10, "Sigil"),
    REAGENT_REAGENT(5, 0, "Reagent"),
    ARROW(6, 2, "Arrow"),
    BULLET(6, 3, "Bullet"),
    TRADE_GOODS(7, 0, "Trade Goods"),
    PARTS(7, 1, "Parts"),
    EXPLOSIVES(7, 2, "Explosives"),
    DEVICES(7, 3, "Devices"),
    JEWELCRAFTING_TRADE(7, 4, "Jewelcrafting"),
    CLOTH_TRADE(7, 5, "Cloth"),
    LEATHER_TRADE(7, 6, "Leather"),
    METAL_AND_STONE(7, 7, "Metal & Stone"),
    MEAT(7, 8, "Meat"),
    HERB(7, 9, "Herb"),
    ELEMENTAL(7, 10, "Elemental"),
    OTHER_TRADE(7, 11, "Other"),
    ENCHANTING_TRADE(7, 12, "Enchanting"),
    MATERIALS(7, 13, "Materials"),
    ARMOR_ENCHANTMENT(7, 14, "Armor Enchantment"),
    WEAPON_ENCHANTMENT(7, 15, "Weapon Enchantment"),
    BOOK(9, 0, "Book"),
    LEATHERWORKING(9, 1, "Leatherworking"),
    TAILORING(9, 2, "Tailoring"),
    ENGINEERING(9, 3, "Engineering"),
    BLACKSMITHING(9, 4, "Blacksmithing"),
    COOKING(9, 5, "Cooking"),
    ALCHEMY(9, 6, "Alchemy"),
    FIRST_AID(9, 7, "First Aid"),
    ENCHANTING_RECIPE(9, 8, "Enchanting"),
    FISHING(9, 9, "Fishing"),
    JEWELCRAFTING_RECIPE(9, 10, "Jewelcrafting"),
    QUIVER(11, 2, "Quiver"),
    AMMO_POUCH(11, 3, "Ammo Pouch"),
    QUEST(12, 0, "Quest"),
    KEY(13, 0, "Key"),
    LOCKPICK(13, 1, "Lockpick"),
    PERMANENT(14, 0, "Permanent"),
    JUNK(15, 0, "Junk"),
    REAGENT_MISC(15, 1, "Reagent"),
    PET(15, 2, "Pet"),
    HOLIDAY(15, 3, "Holiday"),
    OTHER_MISC(15, 4, "Other"),
    MOUNT(15, 5, "Mount"),
    WARRIOR(16, 1, "Warrior"),
    PALADIN(16, 2, "Paladin"),
    HUNTER(16, 3, "Hunter"),
    ROGUE(16, 4, "Rogue"),
    PRIEST(16, 5, "Priest"),
    DEATH_KNIGHT(16, 6, "Death Knight"),
    SHAMAN(16, 7, "Shaman"),
    MAGE(16, 8, "Mage"),
    WARLOCK(16, 9, "Warlock"),
    DRUID(16, 11, "Druid"),;

    private final int subclassIdx;
    private final int classIdx;
    private final String label;

    ItemSubClass(int subclassIdx, int classIdx, String label) {
        this.subclassIdx = subclassIdx;
        this.classIdx = classIdx;
        this.label = label;
    }

    public int getSubclassIdx() {
        return subclassIdx;
    }

    public int getClassIdx() {
        return classIdx;
    }

    public String getLabel() {
        return label;
    }

    public static ItemSubClass getByIndex(int index){
        for (ItemSubClass value : ItemSubClass.values()) {
            if(value.getIdx() == index)
                return value;
        }
        return null;
    }
}
