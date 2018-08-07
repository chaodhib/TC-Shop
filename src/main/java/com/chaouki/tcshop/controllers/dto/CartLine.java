package com.chaouki.tcshop.controllers.dto;

import com.chaouki.tcshop.entities.ItemTemplate;

import java.math.BigDecimal;

public class CartLine {
    private ItemTemplate item;
    private Integer quantity;
    private BigDecimal pricePerUnit;

    public ItemTemplate getItem() {
        return item;
    }

    public void setItem(ItemTemplate item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getSubtotal() {
        return pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }
}