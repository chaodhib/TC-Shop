INSERT INTO tcshop.item_template
(entry, class, subclass, name, displayid, Quality, InventoryType, ItemLevel, RequiredLevel, stackable)
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
    stackable
  FROM world.item_template;