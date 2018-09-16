package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Order;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${stripe.secret.key}")
    String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
        Stripe.setConnectTimeout(30 * 1000);
        Stripe.setReadTimeout(30 * 1000);
    }

    @Override
    public String checkPaymentDetails(Order order, StripePaymentDetails paymentDetails, BigDecimal totalPrice) {

        if(paymentDetails instanceof StripePaymentDetailsStub) {
            order.setStripeChargeId("TEST");
            return STATUS_SUCCESS;
        }

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", totalPrice.multiply(BigDecimal.valueOf(100)).toBigIntegerExact().intValue());
        chargeParams.put("currency", "EUR");
        chargeParams.put("description", "order " + order.getId());
        chargeParams.put("source", paymentDetails.getToken());

        try {
            Charge charge = Charge.create(chargeParams);
            order.setStripeChargeId(charge.getId());
            return STATUS_SUCCESS;
        } catch (StripeException e) {
            if(e instanceof CardException){
                String chargeId = ((CardException) e).getCharge();
                order.setStripeChargeId(chargeId);
            }

            LOGGER.warn("payment failed", e);
            return e.getCode();
        }
    }

    @Override
    public void refundCustomerOfOrder(Order order) {
        Assert.hasText(order.getStripeChargeId(), "the strip charge ID shouldn't be null in order to request a refund!");

        Map<String, Object> params = new HashMap<>();
        params.put("charge", order.getStripeChargeId());
        try {
            Refund refund = Refund.create(params);
        } catch (StripeException e) {
            LOGGER.warn("refund request failed for order " + order.getId(), e);
        }
    }

}
