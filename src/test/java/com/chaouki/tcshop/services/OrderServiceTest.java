package com.chaouki.tcshop.services;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.dao.AccountDao;
import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.dao.OrderDao;
import com.chaouki.tcshop.dao.PurchasableItemDao;
import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.entities.enums.OrderStatus;
import com.chaouki.tcshop.messaging.GearPurchaseProducer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PurchasableItemDao purchasableItemDao;

    @Autowired
    private CharacterDao characterDao;

    @Autowired
    private AccountDao accountDao;

    private PaymentService paymentService;
    private GearPurchaseProducer gearPurchaseProducer;
    private CharacterService characterService;

    @Before
    public void setUp() {
        paymentService = Mockito.mock(PaymentService.class);
        gearPurchaseProducer = Mockito.mock(GearPurchaseProducer.class);
        characterService = Mockito.mock(CharacterService.class);
        orderService = new OrderServiceImpl(orderDao, characterService, gearPurchaseProducer, paymentService);
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void createOrder_success() {
        int characterId = 2;
        StripePaymentDetails paymentDetails = new StripePaymentDetails("token", "tokenType", "email");
        Cart cart = new Cart();
        cart.add(purchasableItemDao.findById(646).get(), 1);
        cart.add(purchasableItemDao.findById(6848).get(), 3);
        Mockito.when(paymentService.checkPaymentDetails(Mockito.any(), Mockito.eq(paymentDetails), Mockito.eq(cart.getTotalPrice()))).thenReturn(PaymentService.STATUS_SUCCESS);
        Mockito.when(characterService.findById(characterId)).thenReturn(characterDao.findById(characterId));

        CreateOrderResult orderResult = orderService.createOrder(characterId, paymentDetails, cart);

        Assert.assertThat(orderResult.getStatus(), is(OrderCreationStatus.SUCCESS));
        Assert.assertThat(orderDao.count(), is(1L));
        Order savedOrder = orderDao.findAll().get(0);
        Assert.assertThat(savedOrder.getStatus(), is(OrderStatus.SENDING));
        Assert.assertThat(savedOrder.getCharacter().getId(), is(characterId));
        Assert.assertThat(savedOrder.getTotalPrice(), is(cart.getTotalPrice()));
        Assert.assertThat(savedOrder.getStripeChargeId(), nullValue());
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Test
    public void createOrder_refused() {
        int characterId = 2;
        StripePaymentDetails paymentDetails = new StripePaymentDetails("token", "tokenType", "email");
        Cart cart = new Cart();
        cart.add(purchasableItemDao.findById(646).get(), 1);
        cart.add(purchasableItemDao.findById(6848).get(), 3);
        Mockito.when(paymentService.checkPaymentDetails(Mockito.any(), Mockito.eq(paymentDetails), Mockito.eq(cart.getTotalPrice()))).thenReturn("fail");
        Mockito.when(characterService.findById(characterId)).thenReturn(characterDao.findById(characterId));

        CreateOrderResult orderResult = orderService.createOrder(characterId, paymentDetails, cart);

        Assert.assertThat(orderResult.getStatus(), is(OrderCreationStatus.PAYMENT_FAILED));
        Assert.assertThat(orderDao.count(), is(1L));
        Order savedOrder = orderDao.findAll().get(0);
        Assert.assertThat(savedOrder.getStatus(), is(OrderStatus.PAYMENT_FAILED));
        Assert.assertThat(savedOrder.getCharacter().getId(), is(characterId));
        Assert.assertThat(savedOrder.getTotalPrice(), is(cart.getTotalPrice()));
        Assert.assertThat(savedOrder.getStripeChargeId(), nullValue());
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Sql("/sql/orders_sample.sql")
    @Test
    public void flagOrderAsAcceptedByMessageBroker() {
        int orderId = 1;
        Order order = orderDao.findById(orderId).get();
        Assert.assertThat(order.getStatus(), is(OrderStatus.SENDING));

        orderService.flagOrderAsAcceptedByMessageBroker(order);

        order = orderDao.findById(orderId).get();
        Assert.assertThat(order.getStatus(), is(OrderStatus.WAITING_FOR_CONFIRMATION));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Sql("/sql/orders_sample.sql")
    @Test
    public void flagOrderAsAcceptedByGameServer() {
        int orderId = 2;
        Order order = orderDao.findById(orderId).get();
        Assert.assertThat(order.getStatus(), is(OrderStatus.WAITING_FOR_CONFIRMATION));

        orderService.flagOrderAsAcceptedByGameServer(order.getId());

        order = orderDao.findById(orderId).get();
        Assert.assertThat(order.getStatus(), is(OrderStatus.DELIVERED));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Sql("/sql/orders_sample.sql")
    @Test
    public void flagOrderAsRefusedByGameServer() {
        int orderId = 2;
        Order order = orderDao.findById(orderId).get();
        Assert.assertThat(order.getStatus(), is(OrderStatus.WAITING_FOR_CONFIRMATION));

        orderService.flagOrderAsRefusedByGameServer(order.getId());

        order = orderDao.findById(orderId).get();
        Assert.assertThat(order.getStatus(), is(OrderStatus.DELIVERY_FAILED));
        Mockito.verify(paymentService, Mockito.times(1)).refundCustomerOfOrder(Mockito.argThat(or -> or.getId() == orderId));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Sql("/sql/orders_sample.sql")
    @Test
    public void findById() {
        Order order = orderService.findById(1);
        Assert.assertThat(order.getStatus(), is(OrderStatus.SENDING));
        Assert.assertThat(order.getCharacter().getId(), is(2));
        Assert.assertThat(order.getDateTime(), is(LocalDateTime.of(2000, 1, 1, 0, 0)));
    }

    @Sql("/sql/cleanAllTables.sql")
    @Test(expected = IllegalArgumentException.class)
    public void findById_doNotExist() {
        orderService.findById(3);
    }

    @Sql("/sql/cleanAllTables.sql")
    @Sql("/sql/accounts_sample.sql")
    @Sql("/sql/characters_sample.sql")
    @Sql("/sql/orders_sample.sql")
    @Test
    public void findByAccount() {
        List<Order> orderList = orderService.findByAccount(accountDao.findById(3).get());
        Assert.assertThat(orderList.size(), is(2));

        Order orderA = orderList.get(0);
        Assert.assertThat(orderA.getId(), is(1));

        Order orderB = orderList.get(1);
        Assert.assertThat(orderB.getId(), is(2));
    }
}