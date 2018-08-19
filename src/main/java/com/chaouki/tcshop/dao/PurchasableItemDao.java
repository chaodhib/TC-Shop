package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.PurchasableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchasableItemDao extends JpaRepository<PurchasableItem, Integer>, JpaSpecificationExecutor<PurchasableItem> {

}
