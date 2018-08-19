package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.PurchasableItemDao;
import com.chaouki.tcshop.entities.PurchasableItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class PurchasableItemServiceImpl implements PurchasableItemService {

    @Autowired
    private PurchasableItemDao purchasableItemDao;

    @Override
    public Optional<PurchasableItem> findById(Integer id) {
        return purchasableItemDao.findById(id);
    }

    @Override
    public Page<PurchasableItem> findAll(Specification<PurchasableItem> spec, Pageable pageable) {
        Assert.notNull(pageable, "there should be a defined page size");
        Assert.isTrue(pageable.getPageSize() <= 50, "the page size should be equal or lower to 50");
        return purchasableItemDao.findAll(spec, pageable);
    }
}
