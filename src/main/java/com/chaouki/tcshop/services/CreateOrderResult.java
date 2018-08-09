package com.chaouki.tcshop.services;

public class CreateOrderResult {

    private OrderCreationStatus status;
    private String errorMessage;

    public CreateOrderResult(OrderCreationStatus status) {
        this.status = status;
    }

    public CreateOrderResult(OrderCreationStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public OrderCreationStatus getStatus() {
        return status;
    }

    public void setStatus(OrderCreationStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
