package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Integer> {

    @Query("SELECT ord FROM Order ord WHERE ord.character.account = ?1 ORDER BY ord.dateTime")
    List<Order> findByAccount(Account account);
}
