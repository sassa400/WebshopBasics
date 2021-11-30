CREATE TYPE order_status AS ENUM('DRAFT', 'SUBMITTED');

CREATE TABLE product
(
    id              serial
        CONSTRAINT product_pk
            PRIMARY KEY,
    code         char(10),
    name         varchar(100),
    price_hrk    bigint  NOT NULL,
    description  varchar(255),
    is_available boolean NOT NULL
);

CREATE TABLE customer
(
    id              serial
        CONSTRAINT customer_pk
            PRIMARY KEY,
    first_name varchar(100),
    last_name  varchar(100),
    email      varchar(100)
);

CREATE TABLE "webshop_order"
(
    id              serial
        CONSTRAINT webshop_order_pk
            PRIMARY KEY,
    customer_id     integer
        CONSTRAINT webshop_order_customer_id_fk
            REFERENCES "customer",
    status          order_status,
    total_price_hrk bigint,
    total_price_eur bigint
);

CREATE TABLE "order_item"
(
    id         serial
        CONSTRAINT orderitem_pk
            PRIMARY KEY,
    order_id   integer
        CONSTRAINT orderitem_webshop_order_id_fk
            REFERENCES "webshop_order",
    product_id integer
        CONSTRAINT orderitem_product_id_fk
            REFERENCES product,
    quantity   integer
);