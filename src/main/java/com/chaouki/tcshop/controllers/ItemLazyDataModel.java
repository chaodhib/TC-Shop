package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.entities.PurchasableItem;
import com.chaouki.tcshop.entities.PurchasableItem;
import com.chaouki.tcshop.entities.enums.InventoryType;
import com.chaouki.tcshop.entities.enums.ItemClass;
import com.chaouki.tcshop.services.PurchasableItemService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemLazyDataModel extends LazyDataModel<PurchasableItem> {

    private final PurchasableItemService purchasableItemService;

    public ItemLazyDataModel(PurchasableItemService purchasableItemService) {
        this.purchasableItemService = purchasableItemService;
    }

    @Override
    public List<PurchasableItem> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        Specification<PurchasableItem> spec;
        spec = (Specification<PurchasableItem>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (filters.containsKey("itemTemplate.entry")) {
                Predicate predicate = criteriaBuilder.equal(root.get("itemTemplate").get("entry"), filters.get("itemTemplate.entry"));
                predicateList.add(predicate);
            }

            if (filters.containsKey("itemTemplate.name")) {
                Predicate predicate = criteriaBuilder.like(root.get("itemTemplate").get("name"), "%" + filters.get("itemTemplate.name") + "%");
                predicateList.add(predicate);
            }

            if (filters.containsKey("itemTemplate.itemClass.idx")) {
                Predicate predicate = criteriaBuilder.equal(root.get("itemTemplate").get("itemClass"), ItemClass.getByIndex(Integer.valueOf((String) filters.get("itemTemplate.itemClass.idx"))));
                predicateList.add(predicate);
            }

            if (filters.containsKey("itemTemplate.inventoryType.idx")) {
                Predicate predicate = criteriaBuilder.equal(root.get("itemTemplate").get("inventoryType"), InventoryType.getByIndex(Integer.valueOf((String) filters.get("itemTemplate.inventoryType.idx"))));
                predicateList.add(predicate);
            }

            if (filters.containsKey("itemTemplate.requiredLevel")) {
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("itemTemplate").get("requiredLevel"), (String) filters.get("itemTemplate.requiredLevel"));
                predicateList.add(predicate);
            }

            Predicate predicate = criteriaBuilder.isTrue(root.get("isAvailable"));
            predicateList.add(predicate);

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };

        Page<PurchasableItem> page = purchasableItemService.findAll(spec, PageRequest.of(first / pageSize, pageSize));

        this.setRowCount(Long.valueOf(page.getTotalElements()).intValue());
        this.setPageSize(page.getNumberOfElements());
        return page.getContent();
    }

    @Override
    public PurchasableItem getRowData(String rowKey) {
        return purchasableItemService.findById(Integer.valueOf(rowKey)).orElse(null);
    }

    @Override
    public Object getRowKey(PurchasableItem object) {
        return object.getId();
    }
}
