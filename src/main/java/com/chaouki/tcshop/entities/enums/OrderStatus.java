package com.chaouki.tcshop.entities.enums;

public enum OrderStatus {

    SENDING (1, "Sending"),
    WAITING_FOR_CONFIRMATION (2, "Waiting for confirmation"),
    DELIVERED (3, "Delivered");

    private final int idx;
    private final String label;

    OrderStatus(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public int getIdx() {
        return idx;
    }

    public String getLabel() {
        return label;
    }

    public static OrderStatus getByIndex(int index){
        for (OrderStatus value : OrderStatus.values()) {
            if(value.getIdx() == index)
                return value;
        }
        return null;
    }
}
