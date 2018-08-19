package com.chaouki.tcshop.controllers.dto;

import com.chaouki.tcshop.entities.ItemTemplate;
import com.chaouki.tcshop.entities.PurchasableItem;

import java.math.BigDecimal;

public class CartLine {

    private Integer quantity;
    private PurchasableItem purchasableItem;

    public BigDecimal getSubtotal() {
        return purchasableItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PurchasableItem getPurchasableItem() {
        return purchasableItem;
    }

    public void setPurchasableItem(PurchasableItem purchasableItem) {
        this.purchasableItem = purchasableItem;
    }
}