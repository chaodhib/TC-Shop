create table account
(
  id              int          not null primary key,
  hashed_password varchar(255) not null,
  username        varchar(255) not null
);

create table character_
(
  id              int          not null primary key,
  name            varchar(255) not null,
  character_class tinyint      not null,
  account_id      int          not null,
  equipment_update_timestamp datetime null,

  FOREIGN KEY FK_character_account (account_id) REFERENCES account (id)
);

create table item_template
(
  entry         mediumint unsigned default '0'   not null primary key,
  class         tinyint unsigned default '0'     not null,
  subclass      tinyint unsigned default '0'     not null,
  name          varchar(255) default ''          not null,
  displayid     mediumint unsigned default '0'   not null,
  Quality       tinyint unsigned default '0'     not null,
  InventoryType tinyint unsigned default '0'     not null,
  ItemLevel     smallint(5) unsigned default '0' not null,
  RequiredLevel tinyint unsigned default '0'     not null,
  stackable     int default '1'                  null
);

create index idx_name
  on item_template (name);

create index items_index
  on item_template (class);

create table character_equipment
(
  character_id       int                not null,
  slot_id            int                not null,
  item_template_id   mediumint unsigned not null,

  PRIMARY KEY  (character_id, slot_id),
  FOREIGN KEY FK_Equipment_ItemTemplate (item_template_id) REFERENCES item_template (entry),
  FOREIGN KEY FK_Equipment_Character (character_id) REFERENCES character_ (id)
);