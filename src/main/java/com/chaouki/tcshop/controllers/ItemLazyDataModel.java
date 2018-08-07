package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.ItemTemplate;
import com.chaouki.tcshop.entities.enums.ItemClass;
import com.chaouki.tcshop.services.ItemTemplateService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemLazyDataModel extends LazyDataModel<ItemTemplate> {

    private final ItemTemplateService itemTemplateService;

    public ItemLazyDataModel(ItemTemplateService itemTemplateService) {
        this.itemTemplateService = itemTemplateService;
    }

    @Override
    public List<ItemTemplate> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        Specification<ItemTemplate> spec = null;
        if(!filters.isEmpty()) {
            spec = (Specification<ItemTemplate>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicateList = new ArrayList<>();

                if(filters.containsKey("entry")) {
                    Predicate predicate = criteriaBuilder.equal(root.get("entry"), filters.get("entry"));
                    predicateList.add(predicate);
                }

                if(filters.containsKey("name")) {
                    Predicate predicate = criteriaBuilder.like(root.get("name"), "%" + filters.get("name") + "%");
                    predicateList.add(predicate);
                }

                if(filters.containsKey("itemClass.idx")) {
                    Predicate predicate = criteriaBuilder.equal(root.get("itemClass"), ItemClass.getByIndex(Integer.valueOf((String) filters.get("itemClass.idx"))));
                    predicateList.add(predicate);
                }

                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
        }

        Page<ItemTemplate> page = itemTemplateService.findAll(spec, PageRequest.of(first / pageSize, pageSize));

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
