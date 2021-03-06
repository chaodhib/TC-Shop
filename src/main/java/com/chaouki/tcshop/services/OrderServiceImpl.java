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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderDao orderDao;
    private CharacterService characterService;
    private GearPurchaseProducer gearPurchaseProducer;
    private PaymentService paymentService;

    public OrderServiceImpl(OrderDao orderDao, CharacterService characterService, GearPurchaseProducer gearPurchaseProducer, PaymentService paymentService) {
        this.orderDao = orderDao;
        this.characterService = characterService;
        this.gearPurchaseProducer = gearPurchaseProducer;
        this.paymentService = paymentService;
    }

    @Override
    public CreateOrderResult createOrder(Integer characterId, StripePaymentDetails paymentDetails, Cart cart) {
        Character character = characterService.findById(characterId).orElseThrow(IllegalArgumentException::new);
        Assert.notEmpty(cart.getCartLines(), "the cart shouldn't be empty");

        Order order = persistOrder(character, cart);

        String paymentStatus = paymentService.checkPaymentDetails(order, paymentDetails, cart.getTotalPrice());
        if (!PaymentService.STATUS_SUCCESS.equals(paymentStatus)) {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderDao.save(order);
            return new CreateOrderResult(OrderCreationStatus.PAYMENT_FAILED, paymentStatus);
        }

        order.setStatus(OrderStatus.SENDING);
        orderDao.save(order);
        deliverItems(order);

        return new CreateOrderResult(OrderCreationStatus.SUCCESS);
    }

    private Order persistOrder(Character character, Cart cart) {
        Order order = new Order();
        order.setCharacter(character);
        order.setDateTime(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setOrderLineList(getOrderLineList(cart, order));

        return orderDao.save(order);
    }

    private ArrayList<OrderLine> getOrderLineList(Cart cart, Order order) {
        ArrayList<OrderLine> orderLines = new ArrayList<>();
        for (CartLine cartLine : cart.getCartLines()) {
            Integer countPerStackMax = cartLine.getPurchasableItem().getItemTemplate().getCountPerStackMax();

            int nbOfStacks = cartLine.getQuantity()/countPerStackMax;
            // create nbOfStacks stacks, with each stack on maximum amount.
            for (int i = 0; i < nbOfStacks; i++) {
                OrderLine orderLine = new OrderLine();
                orderLine.setPurchasableItem(cartLine.getPurchasableItem());
                orderLine.setQuantity(countPerStackMax);
                orderLine.setOrder(order);
                orderLines.add(orderLine);
            }

            // create up to 1 stack of non maximum amount
            if(cartLine.getQuantity() % countPerStackMax != 0) {
                OrderLine orderLine = new OrderLine();
                orderLine.setPurchasableItem(cartLine.getPurchasableItem());
                orderLine.setQuantity(cartLine.getQuantity() % countPerStackMax);
                orderLine.setOrder(order);
                orderLines.add(orderLine);
            }
        }
        return orderLines;
    }

    private void deliverItems(Order order) {
        gearPurchaseProducer.sendGearPurchaseMessage(order, (metadata, exception) -> {
            if (exception == null)
                flagOrderAsAcceptedByMessageBroker(order);
            else
                LOGGER.warn("Could not send Gear Purchase message to the broker", exception);
        });
    }

    @Override
    public void flagOrderAsAcceptedByMessageBroker(Order order) {
        // special rare case when the GEAR_PURCHASE_ACK is processed before the callback of the GEAR_PURCHASE message.
        if(order.getStatus().equals(OrderStatus.DELIVERED) || order.getStatus().equals(OrderStatus.DELIVERY_FAILED))
            return;

        if(!order.getStatus().equals(OrderStatus.SENDING))
            throw new IllegalStateException("orderId " +order.getId());

        order.setStatus(OrderStatus.WAITING_FOR_CONFIRMATION);
        orderDao.save(order);
    }

    @Override
    public void flagOrderAsAcceptedByGameServer(Integer orderId) {
        Order order = orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);

        // handle duplicate messages case
        if(order.getStatus().equals(OrderStatus.DELIVERED))
            return;

        // special rare case when the GEAR_PURCHASE_ACK is processed before the callback of the GEAR_PURCHASE message.
        if(!order.getStatus().equals(OrderStatus.WAITING_FOR_CONFIRMATION) && !order.getStatus().equals(OrderStatus.SENDING))
            throw new IllegalStateException("orderId " +order.getId());

        order.setStatus(OrderStatus.DELIVERED);
        orderDao.save(order);
    }

    @Override
    public void flagOrderAsRefusedByGameServer(Integer orderId) {
        Order order = orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);

        // handle duplicate messages case
        if(order.getStatus().equals(OrderStatus.DELIVERY_FAILED))
            return;

        if(!order.getStatus().equals(OrderStatus.WAITING_FOR_CONFIRMATION))
            throw new IllegalStateException("orderId " +order.getId());

        paymentService.refundCustomerOfOrder(order);

        order.setStatus(OrderStatus.DELIVERY_FAILED);
        orderDao.save(order);
    }

    @Override
    public Order findById(Integer id) {
        return orderDao.findById(id).orElseThrow(() -> new IllegalArgumentException("orderId " + id));
    }

    @Override
    public List<Order> findByAccount(Account account) {
        return orderDao.findByAccount(account);
    }
}
