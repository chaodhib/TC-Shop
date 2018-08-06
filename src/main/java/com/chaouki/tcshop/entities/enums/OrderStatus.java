package com.chaouki.tcshop.entities.enums;

public enum OrderStatus {

    WAITING_FOR_PAYMENT (1),
    WAITING_FOR_SENDING (2),
    WAITING_FOR_CONFIRMATION (3),
    DELIVERED (4);

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
