package com.chaouki.tcshop.entities;

/**
 * Created by chaouki on 20/03/2018.
 */
public enum Race
{
    RACE_HUMAN              (1),
    RACE_ORC                (2),
    RACE_DWARF              (3),
    RACE_NIGHTELF           (4),
    RACE_UNDEAD_PLAYER      (5),
    RACE_TAUREN             (6),
    RACE_GNOME              (7),
    RACE_TROLL              (8),
    RACE_BLOODELF           (10),
    RACE_DRAENEI            (11);

    private final int idx;

    Race(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public static Race getByIndex(int index){
        for (Race race : Race.values()) {
            if(race.getIdx() == index)
                return race;
        }
        return null;
    }
}