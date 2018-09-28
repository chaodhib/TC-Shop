SET FOREIGN_KEY_CHECKS=0;

TRUNCATE `shop_order_line`;
TRUNCATE `shop_order`;
TRUNCATE `character_equipment`;
TRUNCATE `character_`;
DELETE FROM `tcshop`.`account` WHERE id != 2147483647;
