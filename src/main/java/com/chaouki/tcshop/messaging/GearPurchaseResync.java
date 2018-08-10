package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.dao.OrderDao;
import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.entities.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GearPurchaseResync {

    @Autowired
    private GearPurchaseProducer gearPurchaseProducer;

    @Autowired
    private OrderDao orderDao;

    @Scheduled(fixedRate = 1000 * 3600, initialDelay = 0) // starts first execution as possible. then execute each hour.
    public void syncPendingGearPurchaseMessages() {
        Page<Order> orderPage;
        int page = 0;
        do {
            Specification<Order> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), OrderStatus.SENDING);
            orderPage = orderDao.findAll(specification, PageRequest.of(page, 50));
            for (Order order : orderPage)
                gearPurchaseProducer.sendGearPurchaseMessage(order);
            page++;
        } while (orderPage.hasNext());
    }
}
