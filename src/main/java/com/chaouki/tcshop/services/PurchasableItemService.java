package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.PurchasableItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface PurchasableItemService {

    Optional<PurchasableItem> findById(Integer id);

    Page<PurchasableItem> findAll(@Nullable Specification<PurchasableItem> spec, Pageable pageable);
}
