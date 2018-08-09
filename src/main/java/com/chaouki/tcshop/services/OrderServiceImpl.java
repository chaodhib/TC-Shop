package com.chaouki.tcshop.services;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.controllers.dto.CartLine;
import com.chaouki.tcshop.dao.OrderDao;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.entities.OrderLine;
import com.chaouki.tcshop.entities.enums.OrderStatus;
import com.chaouki.tcshop.messaging.GearPurchaseProducer;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    public static final String STATUS_SUCCESS = "success";

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private GearPurchaseProducer gearPurchaseProducer;

    @Value("${stripe.secret.key}")
    String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
        Stripe.setConnectTimeout(30 * 1000);
        Stripe.setReadTimeout(30 * 1000);
    }

    @Override
    public CreateOrderResult createOrder(Integer characterId, StripePaymentDetails paymentDetails, Cart cart) {
        Character character = characterService.findById(characterId).orElseThrow(IllegalArgumentException::new);
        Assert.notEmpty(cart.getCartLines(), "the cart shouldn't be empty");

        Order order = persistOrder(character, cart);

        String paymentStatus = checkPaymentDetails(order, paymentDetails, cart.getTotalPrice());
        if (!STATUS_SUCCESS.equals(paymentStatus)) {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderDao.save(order);
            return new CreateOrderResult(OrderCreationStatus.PAYMENT_FAILED, paymentStatus);
        }

        order.setStatus(OrderStatus.SENDING);
        orderDao.save(order);
        deliverItems(order);

        return new CreateOrderResult(OrderCreationStatus.SUCCESS);
    }

    private String checkPaymentDetails(Order order, StripePaymentDetails paymentDetails, BigDecimal totalPrice) {
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

    private Order persistOrder(Character character, Cart cart) {
        Order order = new Order();
        order.setCharacter(character);
        order.setDateTime(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        order.setStatus(OrderStatus.PAYMENT_VALIDATION);
        order.setOrderLineList(getOrderLineList(cart, order));

        return orderDao.save(order);
    }

    private ArrayList<OrderLine> getOrderLineList(Cart cart, Order order) {
        ArrayList<OrderLine> orderLines = new ArrayList<>();
        for (CartLine cartLine : cart.getCartLines()) {
            Integer countPerStackMax = cartLine.getItem().getCountPerStackMax();

            int nbOfStacks = cartLine.getQuantity()/countPerStackMax;
            // create nbOfStacks stacks, with each stack on maximum amount.
            for (int i = 0; i < nbOfStacks; i++) {
                OrderLine orderLine = new OrderLine();
                orderLine.setItem(cartLine.getItem());
                orderLine.setQuantity(cartLine.getQuantity());
                orderLine.setUnitPrice(cartLine.getPricePerUnit());
                orderLine.setOrder(order);
                orderLines.add(orderLine);
            }

            // create up to 1 stack of non maximum amount
            if(cartLine.getQuantity() % countPerStackMax != 0) {
                OrderLine orderLine = new OrderLine();
                orderLine.setItem(cartLine.getItem());
                orderLine.setQuantity(cartLine.getQuantity() % countPerStackMax);
                orderLine.setUnitPrice(cartLine.getPricePerUnit());
                orderLine.setOrder(order);
                orderLines.add(orderLine);
            }
        }
        return orderLines;
    }

    private void deliverItems(Order order) {
        gearPurchaseProducer.sendGearPurchaseMessage(order);
    }

    @Override
    public void flagOrderAsSentToMessageBroker(Order order) {
        if(!order.getStatus().equals(OrderStatus.SENDING))
            throw new IllegalStateException("orderId " +order.getId());

        order.setStatus(OrderStatus.WAITING_FOR_DELIVERY);
        orderDao.save(order);
    }

    @Override
    public void flagOrderAsSentToGameServer(Order order) {
        if(!order.getStatus().equals(OrderStatus.WAITING_FOR_DELIVERY))
            throw new IllegalStateException("orderId " +order.getId());

        order.setStatus(OrderStatus.DELIVERED);
        orderDao.save(order);
    }

    @Override
    public Order findById(Integer id) {
        return orderDao.findById(id).orElseThrow(() -> new IllegalStateException("orderId " + id));
    }

    @Override
    public List<Order> findByAccount(Account account) {
        return orderDao.findByAccount(account);
    }
}
