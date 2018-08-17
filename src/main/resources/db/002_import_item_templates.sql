INSERT INTO tcshop.item_template
(entry, class, subclass, name, displayid, quality, inventory_type, item_level, required_level, stackable, is_purchasable, current_unit_price)
  SELECT
    entry,
    class,
    subclass,
    name,
    displayid,
    Quality,
    InventoryType,
    ItemLevel,
    RequiredLevel,
    stackable,
    1,
    GREATEST(ItemLevel / 10, 1)
  FROM world.item_template;