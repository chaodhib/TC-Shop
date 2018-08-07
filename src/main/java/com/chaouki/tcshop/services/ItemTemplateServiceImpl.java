package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.ItemTemplateDao;
import com.chaouki.tcshop.entities.ItemTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemTemplateServiceImpl implements ItemTemplateService {

    @Autowired
    private ItemTemplateDao itemTemplateDao;

    @Override
    public Optional<ItemTemplate> findById(Integer id) {
        return itemTemplateDao.findById(id);
    }

    @Override
    public Page<ItemTemplate> findAll(Specification<ItemTemplate> spec, Pageable pageable) {
        return itemTemplateDao.findAll(spec, pageable);
    }
}
