package com.chaouki.tcshop.services;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.entities.Order;

public interface OrderService {
    OrderCreationStatus createOrder(Integer characterId, String paymentDetails, Cart cart);

    void flagOrderAsSentToMessageBroker(Order order);

    void flagOrderAsSentToGameServer(Order order);
}
