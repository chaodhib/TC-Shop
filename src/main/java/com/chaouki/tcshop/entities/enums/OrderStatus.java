package com.chaouki.tcshop.entities.enums;

public enum OrderStatus {

    SENDING (1),
    WAITING_FOR_CONFIRMATION (2),
    DELIVERED (3);

    private final int idx;

    OrderStatus(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public static OrderStatus getByIndex(int index){
        for (OrderStatus value : OrderStatus.values()) {
            if(value.getIdx() == index)
                return value;
        }
        return null;
    }
}
