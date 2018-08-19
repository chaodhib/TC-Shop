package com.chaouki.tcshop.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "shop_order_line")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "purchasable_item_id")
    private PurchasableItem purchasableItem;

    @ManyToOne
    private Order order;

    public BigDecimal getSubtotal() {
        return purchasableItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PurchasableItem getPurchasableItem() {
        return purchasableItem;
    }

    public void setPurchasableItem(PurchasableItem purchasableItem) {
        this.purchasableItem = purchasableItem;
    }
}
