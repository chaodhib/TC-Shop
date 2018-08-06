package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Integer> {
}
