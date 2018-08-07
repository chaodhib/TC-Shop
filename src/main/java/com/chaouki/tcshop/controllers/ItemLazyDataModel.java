package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.ItemTemplate;
import com.chaouki.tcshop.services.ItemTemplateService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public class ItemLazyDataModel extends LazyDataModel<ItemTemplate> {

    private final ItemTemplateService itemTemplateService;

    public ItemLazyDataModel(ItemTemplateService itemTemplateService) {
        this.itemTemplateService = itemTemplateService;
    }

    @Override
    public List<ItemTemplate> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Page<ItemTemplate> page = itemTemplateService.findAll(null, PageRequest.of(first/pageSize, pageSize));
        this.setRowCount(Long.valueOf(page.getTotalElements()).intValue());
        this.setPageSize(page.getNumberOfElements());
        return page.getContent();
    }

    @Override
    public ItemTemplate getRowData(String rowKey) {
        return itemTemplateService.findById(Integer.valueOf(rowKey)).orElse(null);
    }

    @Override
    public Object getRowKey(ItemTemplate object) {
        return object.getEntry();
    }
}
