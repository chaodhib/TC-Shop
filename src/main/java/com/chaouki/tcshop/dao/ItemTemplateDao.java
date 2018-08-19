package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.ItemTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemTemplateDao extends JpaRepository<ItemTemplate, Integer> {

}
