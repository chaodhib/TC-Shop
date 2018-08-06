package com.chaouki.tcshop.entities.enums;

import java.awt.*;

public enum ItemQuality {

    POOR            (0, new Color(157, 157, 157), "Poor"),
    COMMON          (1, new Color(255, 255, 255), "Common"),
    UNCOMMON        (2, new Color(30, 255, 0), "Uncommon"),
    RARE            (3, new Color(0, 112, 221), "Rare"),
    EPIC            (4, new Color(163, 53, 238), "Epic"),
    LEGENDARY       (5, new Color(255, 128, 0), "Legendary"),
    ARTIFACT        (6, new Color(230, 204, 128), "Artifact"),
    BIND_TO_ACCOUNT (7, new Color(0, 204, 255), "Bind to Account");

    private final int idx;
    private final Color color;
    private final String label;

    ItemQuality(int idx, Color color, String label) {
        this.idx = idx;
        this.color = color;
        this.label = label;
    }

    public int getIdx() {
        return idx;
    }

    public Color getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    public static ItemQuality getByIndex(int index){
        for (ItemQuality value : ItemQuality.values()) {
            if(value.getIdx() == index)
                return value;
        }
        return null;
    }
}
