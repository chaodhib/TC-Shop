INSERT INTO shop_order (id, date_time, status, character_id, stripe_charge_id) VALUES
(1, '2000-01-01 00:00:00', 3, 2, null),
(2, '2000-01-02 00:00:00', 4, 2, null);

INSERT INTO shop_order_line (quantity, purchasable_item_id, order_id) VALUES
(1, 4132, 1),
(1, 559, 1),
(1, 4646, 1),
(1, 559, 2),
(1, 4646, 2);