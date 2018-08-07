package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.ItemTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ItemCatalogServiceImpl implements ItemCatalogService {

    @Override
    public BigDecimal getItemPricePerUnit(ItemTemplate itemTemplate) {
        return BigDecimal.valueOf(itemTemplate.getItemLevel()).divide(BigDecimal.TEN, RoundingMode.FLOOR).max(BigDecimal.ONE);
    }
}
