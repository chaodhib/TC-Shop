package com.chaouki.tcshop.services;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Order;

import java.util.List;

public interface OrderService {
    CreateOrderResult createOrder(Integer characterId, StripePaymentDetails paymentDetails, Cart cart);

    void flagOrderAsAcceptedByMessageBroker(Order order);

    void flagOrderAsAcceptedByGameServer(Integer order);

    void flagOrderAsRefusedByGameServer(Integer orderId);

    Order findById(Integer id);

    List<Order> findByAccount(Account account);
}
