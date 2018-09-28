package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.dao.OrderDao;
import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.entities.enums.OrderStatus;
import com.chaouki.tcshop.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GearPurchaseResync {

    private static final Logger LOGGER = LoggerFactory.getLogger(GearPurchaseProducer.class);

    @Autowired
    private GearPurchaseProducer gearPurchaseProducer;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 1000 * 3600, initialDelay = 0) // starts first execution as soon as possible. then execute each hour.
    public void syncPendingGearPurchaseMessages() {

        LOGGER.info("Syncing pending gear purchase messages");

        Page<Order> orderPage;
        int page = 0;
        do {
            Specification<Order> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), OrderStatus.SENDING);
            orderPage = orderDao.findAll(specification, PageRequest.of(page, 50));
            for (Order order : orderPage)
                gearPurchaseProducer.sendGearPurchaseMessage(order, (metadata, exception) -> {
                    if (exception == null)
                        orderService.flagOrderAsAcceptedByMessageBroker(order);
                    else
                        LOGGER.warn("Could not send Gear Purchase message to the broker", exception);
                });
            page++;
        } while (orderPage.hasNext());
    }
}
