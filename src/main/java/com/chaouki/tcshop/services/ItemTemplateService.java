package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.ItemTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface ItemTemplateService {

    Optional<ItemTemplate> findById(Integer id);

    Page<ItemTemplate> findAll(@Nullable Specification<ItemTemplate> spec, Pageable pageable);
}
