package com.chaouki.tcshop.services;

import com.chaouki.tcshop.controllers.dto.Cart;

public interface OrderService {
    OrderCreationStatus createOrder(Integer characterId, String paymentDetails, Cart cart);
}
