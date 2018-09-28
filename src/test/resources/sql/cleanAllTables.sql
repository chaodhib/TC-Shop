SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `tcshop`.`account` WHERE id != 2147483647;
TRUNCATE `tcshop`.`character_`;
TRUNCATE `tcshop`.`character_equipment`;
TRUNCATE `tcshop`.`shop_order`;
TRUNCATE `tcshop`.`shop_order_line`;

TRUNCATE `characters`.`processed_shop_orders`;
UPDATE `characters`.`characters` SET kafka_synced = 0, gear_kafka_synced = 0;
UPDATE `auth`.`account` SET kafka_synced = 0;


SET FOREIGN_KEY_CHECKS=1;

DELETE FROM characters.mail;
DELETE FROM characters.mail_items;