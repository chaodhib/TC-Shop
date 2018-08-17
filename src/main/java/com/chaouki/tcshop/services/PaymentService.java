package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Order;

import java.math.BigDecimal;

public interface PaymentService {

    String STATUS_SUCCESS = "success";

    String checkPaymentDetails(Order order, StripePaymentDetails paymentDetails, BigDecimal totalPrice);

    void refundCustomerOfOrder(Order order);
}
