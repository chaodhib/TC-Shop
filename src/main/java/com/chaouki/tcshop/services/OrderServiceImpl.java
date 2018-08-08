package com.chaouki.tcshop.services;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.controllers.dto.CartLine;
import com.chaouki.tcshop.dao.OrderDao;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.entities.OrderLine;
import com.chaouki.tcshop.entities.enums.OrderStatus;
import com.chaouki.tcshop.messaging.GearPurchaseProducer;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private GearPurchaseProducer gearPurchaseProducer;

    @Override
    public OrderCreationStatus createOrder(Integer characterId, String paymentDetails, Cart cart) {
        Character character = characterService.findById(characterId).orElseThrow(IllegalArgumentException::new);
        Assert.notEmpty(cart.getCartLines(), "the cart shouldn't be empty");

        PaymentCheckStatus paymentCheckStatus = checkPaymentDetails(paymentDetails, cart.getTotalPrice());
        if (!paymentCheckStatus.equals(PaymentCheckStatus.SUCCESS)) {

            return OrderCreationStatus.PAYMENT_FAILED;
        }

        Order order = persistOrder(character, cart);
        deliverItems(order);

        return OrderCreationStatus.SUCCESS;
    }

    private PaymentCheckStatus checkPaymentDetails(String paymentDetails, BigDecimal totalPrice) {
        return PaymentCheckStatus.SUCCESS;
    }

    private Order persistOrder(Character character, Cart cart) {
        Order order = new Order();
        order.setCharacter(character);
        order.setDateTime(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        order.setStatus(OrderStatus.SENDING);
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

        order.setStatus(OrderStatus.WAITING_FOR_CONFIRMATION);
        orderDao.save(order);
    }

    @Override
    public void flagOrderAsSentToGameServer(Order order) {
        if(!order.getStatus().equals(OrderStatus.WAITING_FOR_CONFIRMATION))
            throw new IllegalStateException("orderId " +order.getId());

        order.setStatus(OrderStatus.DELIVERED);
        orderDao.save(order);
    }

    @Override
    public Order findById(Integer id) {
        return orderDao.findById(id).orElseThrow(() -> new IllegalStateException("orderId " + id));
    }
}
