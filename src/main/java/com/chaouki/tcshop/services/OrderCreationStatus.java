package com.chaouki.tcshop.services;

public enum OrderCreationStatus {
    SUCCESS ("success"),
    PAYMENT_FAILED("Payment failed");

    private final String label;

    OrderCreationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
