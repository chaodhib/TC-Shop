INSERT INTO account (id, hashed_password, username, last_update_timestamp, is_stub)
    VALUE (2147483647, 'STUB@ACCOUNT', 'STUB@ACCOUNT', '2000-01-01 00:00:00', 1); # id = max int value

INSERT INTO tcshop.item_template
(entry, class, subclass, name, displayid, quality, inventory_type, item_level, required_level, stackable)
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

INSERT INTO tcshop.purchasable_item (item_template_id, is_available, unit_price)
  SELECT
    entry,
    1,
    GREATEST(item_level / 10, 1)
  FROM tcshop.item_template;

UPDATE purchasable_item SET is_available = 0 WHERE item_template_id = 17