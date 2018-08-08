package com.chaouki.tcshop.entities;

import com.chaouki.tcshop.controllers.dto.CartLine;
import com.chaouki.tcshop.entities.converters.OrderStatusConverter;
import com.chaouki.tcshop.entities.enums.OrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shop_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "order")
    private List<OrderLine> orderLineList;

    @ManyToOne
    private Character character;

    private LocalDateTime dateTime;

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine orderLine : orderLineList) {
            total = total.add(orderLine.getSubtotal());
        }

        return total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderLine> getOrderLineList() {
        return orderLineList;
    }

    public void setOrderLineList(List<OrderLine> orderLineList) {
        this.orderLineList = orderLineList;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
