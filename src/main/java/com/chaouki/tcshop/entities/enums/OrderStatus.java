package com.chaouki.tcshop.entities.enums;

public enum OrderStatus {

    PAYMENT_VALIDATION (1, "Sending"),
    PAYMENT_FAILED (2, "Payment failed"),
    SENDING (3, "Sending"),
    WAITING_FOR_DELIVERY (4, "Waiting for delivery"),
    DELIVERED (5, "Delivered");

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
