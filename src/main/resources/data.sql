TRUNCATE TABLE product RESTART IDENTITY CASCADE;

INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('AAAAAAAAAA', 'ProductA', 10000, 'Product A', true);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('BBBBBBBBBB', 'ProductB', 12345, 'Product A', true);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('CCCCCCCCCC', 'ProductC', 999999, 'Product A', true);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('DDDDDDDDDD', 'ProductD', 1, 'Product A', true);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('EEEEEEEEEE', 'ProductE', 2, 'Product A', true);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('FFFFFFFFFF', 'ProductF', 100, 'Product A', false);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('GGGGGGGGGG', 'ProductG', 100, 'Product A', false);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('HHHHHHHHHH', 'ProductH', 100, 'Product A', false);
INSERT INTO product (code, name, price_hrk, description, is_available) VALUES ('IIIIIIIIII', 'ProductI', 100, 'Product A', false);

TRUNCATE TABLE customer RESTART IDENTITY CASCADE;

INSERT INTO customer (first_name, last_name, email) VALUES ('Xaxa', 'Xixi', 'xaxa@x.com');
INSERT INTO customer (first_name, last_name, email) VALUES ('Yaya', 'Yiyi', 'yaya@y.com');
INSERT INTO customer (first_name, last_name, email) VALUES ('Zaza', 'Zizi', 'zaza@z.com');

TRUNCATE TABLE webshop_order RESTART IDENTITY CASCADE;

INSERT INTO webshop_order (customer_id, status, total_price_hrk, total_price_eur) VALUES (1, 'DRAFT', 0, 0);

TRUNCATE TABLE order_item RESTART IDENTITY CASCADE;

INSERT INTO order_item (order_id, product_id, quantity) VALUES (1, 1, 12);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (1, 2, 10);
