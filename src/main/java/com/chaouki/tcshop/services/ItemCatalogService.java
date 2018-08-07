package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.ItemTemplate;

import java.math.BigDecimal;

public interface ItemCatalogService {

    BigDecimal getItemPricePerUnit(ItemTemplate itemTemplate);
}
