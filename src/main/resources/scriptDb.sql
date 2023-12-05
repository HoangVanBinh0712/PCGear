create table if not exists pcgear.category
(
    category_id      int auto_increment
    primary key,
    name             varchar(255) charset utf8mb4 null,
    deleteFg         bit default b'0'             null,
    creataedDateTime datetime                     null,
    createdBy        varchar(255) charset utf8mb4 null,
    updatedDateTime  datetime                     null,
    updatedBy        varchar(255) charset utf8mb4 null
    );

create table if not exists pcgear.customer
(
    customer_id      int auto_increment
    primary key,
    name             varchar(255) charset utf8mb4  null,
    email            varchar(255) charset utf8mb4  null,
    password         varchar(255) charset utf8mb4  null,
    address          varchar(1000) charset utf8mb4 null,
    phone_number     varchar(20) charset utf8mb4   null,
    creataedDateTime datetime                      null,
    createdBy        varchar(255) charset utf8mb4  null,
    updatedDateTime  datetime                      null,
    updatedBy        varchar(255) charset utf8mb4  null
    );

create table if not exists pcgear.customer_order
(
    order_id    int auto_increment
    primary key,
    customer_id int                           null,
    order_date  datetime                      null,
    note        varchar(1000) charset utf8mb4 null,
    total_price decimal(18, 2)                null,
    FOREIGN KEY (customer_id) REFERENCES Customer (customer_id)
    );

create table if not exists pcgear.payment
(
    payment_id       int auto_increment
    primary key,
    payment_date     datetime                     null,
    payment_method   varchar(255) charset utf8mb4 null,
    amount           decimal(18, 2)               null,
    customer_id      int                          null,
    order_id         int                          null,
    creataedDateTime datetime                     null,
    createdBy        varchar(255) charset utf8mb4 null,
    updatedDateTime  datetime                     null,
    updatedBy        varchar(255) charset utf8mb4 null,
    foreign key (order_id) references pcgear.customer_order (order_id),
    foreign key (customer_id) references pcgear.customer (customer_id)
    );

create table if not exists pcgear.product
(
    product_id       int auto_increment
    primary key,
    category_id      int                           null,
    description      text                          null,
    price            decimal(14, 2)                null,
    stock            int default 0                 null,
    discount         decimal(4, 2)                 null,
    discountFrom     datetime                      null,
    discountTo       datetime                      null,
    image            varchar(1000) charset utf8mb4 null,
    deleteFg         bit default b'0'              null,
    creataedDateTime datetime                      null,
    createdBy        varchar(255) charset utf8mb4  null,
    updatedDateTime  datetime                      null,
    updatedBy        varchar(255) charset utf8mb4  null,
    foreign key (category_id) references pcgear.category (category_id)
    );

create table if not exists pcgear.cart
(
    cart_id          int auto_increment
    primary key,
    customer_id      int                          null,
    product_id       int                          null,
    quantity         int                          null,
    creataedDateTime datetime                     null,
    createdBy        varchar(255) charset utf8mb4 null,
    updatedDateTime  datetime                     null,
    updatedBy        varchar(255) charset utf8mb4 null,
    foreign key (product_id) references pcgear.product (product_id),
    foreign key (customer_id) references pcgear.customer (customer_id)
    );

create table if not exists pcgear.order_item
(
    order_item_id    int auto_increment
    primary key,
    order_id         int                          null,
    product_id       int                          null,
    quantity         int                          null,
    price            decimal(14, 2)               null,
    creataedDateTime datetime                     null,
    createdBy        varchar(255) charset utf8mb4 null,
    updatedDateTime  datetime                     null,
    updatedBy        varchar(255) charset utf8mb4 null,
    foreign key (order_id) references pcgear.customer_order (order_id),
    foreign key (product_id) references pcgear.product (product_id)
    );


create table if not exists pcgear.shipment
(
    shipment_id      int auto_increment
    primary key,
    shipment_date    datetime                     null,
    address          varchar(255) charset utf8mb4 null,
    city             varchar(255) charset utf8mb4 null,
    state            varchar(255) charset utf8mb4 null,
    country          varchar(255) charset utf8mb4 null,
    zip_code         varchar(20) charset utf8mb4  null,
    customer_id      int                          null,
    order_id         int                          null,
    creataedDateTime datetime                     null,
    createdBy        varchar(255) charset utf8mb4 null,
    updatedDateTime  datetime                     null,
    updatedBy        varchar(255) charset utf8mb4 null,
    foreign key (order_id) references pcgear.customer_order (order_id),
    foreign key (customer_id) references pcgear.customer (customer_id)
    );

create table if not exists pcgear.wishlist
(
    wishlist_id      int auto_increment
    primary key,
    customer_id      int                          null,
    product_id       int                          null,
    creataedDateTime datetime                     null,
    createdBy        varchar(255) charset utf8mb4 null,
    updatedDateTime  datetime                     null,
    updatedBy        varchar(255) charset utf8mb4 null,
    foreign key (product_id) references pcgear.product (product_id),
    foreign key (customer_id) references pcgear.customer (customer_id)
    );

