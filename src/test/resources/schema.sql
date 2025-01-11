create table category
(
    category_id      bigint auto_increment
        primary key,
    created_by       varchar(255) null,
    created_datetime datetime(6)  null,
    updated_by       varchar(255) null,
    updated_datetime datetime(6)  null,
    category_cd      varchar(255) null,
    delete_fg        bit          null,
    description      varchar(255) null,
    name             varchar(255) null
);

create table department
(
    department_id    bigint auto_increment
        primary key,
    created_by       varchar(255)  null,
    created_datetime datetime(6)   null,
    updated_by       varchar(255)  null,
    updated_datetime datetime(6)   null,
    department_cd    varchar(16)   null,
    department_name  varchar(255)  null,
    description      varchar(2000) null
);

create table admin
(
    id               bigint auto_increment
        primary key,
    created_by       varchar(255) null,
    created_datetime datetime(6)  null,
    updated_by       varchar(255) null,
    updated_datetime datetime(6)  null,
    email            varchar(255) null,
    name             varchar(255) null,
    password         varchar(255) null,
    phone_number     varchar(255) null,
    user_name        varchar(255) null,
    department_id    bigint       null,
    constraint FKnmmt6f2kg0oaxr11uhy7qqf3w
        foreign key (department_id) references department (department_id)
);

create table customer
(
    customer_id      bigint auto_increment
        primary key,
    created_by       varchar(255) null,
    created_datetime datetime(6)  null,
    updated_by       varchar(255) null,
    updated_datetime datetime(6)  null,
    address          varchar(255) null,
    email            varchar(255) null,
    name             varchar(255) null,
    password         varchar(255) null,
    phone_number     varchar(255) null,
    user_name        varchar(255) null,
    department_id    bigint       null,
    constraint FK5b3xqcpp6nvpm64shn5yjks1q
        foreign key (department_id) references department (department_id)
);

create table customer_order
(
    order_id         bigint auto_increment
        primary key,
    created_by       varchar(255)   null,
    created_datetime datetime(6)    null,
    updated_by       varchar(255)   null,
    updated_datetime datetime(6)    null,
    note             varchar(255)   null,
    order_date       datetime(6)    null,
    total_amount     decimal(38, 2) null,
    customer_id      bigint         null,
    constraint FKf9abd30bhiqvugayxlpq8ryq9
        foreign key (customer_id) references customer (customer_id)
);

create table payment
(
    payment_id       bigint auto_increment
        primary key,
    created_by       varchar(255)   null,
    created_datetime datetime(6)    null,
    updated_by       varchar(255)   null,
    updated_datetime datetime(6)    null,
    amount           decimal(38, 2) null,
    payment_date     datetime(6)    null,
    payment_method   varchar(255)   null,
    customer_id      bigint         null,
    order_id         bigint         null,
    constraint FKby2skjf3ov608yb6nm16b49lg
        foreign key (customer_id) references customer (customer_id),
    constraint FKjb82tm1s8qg4mx6eqog4fsctj
        foreign key (order_id) references customer_order (order_id)
);

create table permission
(
    permission_id    bigint auto_increment
        primary key,
    created_by       varchar(255)  null,
    created_datetime datetime(6)   null,
    updated_by       varchar(255)  null,
    updated_datetime datetime(6)   null,
    description      varchar(2000) null
);

create table permission_assign
(
    id               bigint auto_increment
        primary key,
    created_by       varchar(255) null,
    created_datetime datetime(6)  null,
    updated_by       varchar(255) null,
    updated_datetime datetime(6)  null,
    department_id    bigint       null,
    end_date         date         not null,
    permission_id    bigint       null,
    start_date       date         not null,
    constraint FK7kaw8tddwvarrecaf1o70hhc4
        foreign key (department_id) references department (department_id),
    constraint FK918sy6hpvjnrbft7gqm8yo6cp
        foreign key (permission_id) references permission (permission_id)
);

create table product
(
    product_id       bigint auto_increment
        primary key,
    created_by       varchar(255)   null,
    created_datetime datetime(6)    null,
    updated_by       varchar(255)   null,
    updated_datetime datetime(6)    null,
    delete_fg        bit            null,
    description      varchar(10000) null,
    discount         decimal(3)     null,
    discount_from    datetime(6)    null,
    discount_to      datetime(6)    null,
    image            varchar(255)   null,
    price            decimal(38, 2) null,
    product_code     varchar(255)   null,
    stock            int            null,
    title            varchar(1000)  not null,
    category_id      bigint         not null,
    constraint FK1mtsbur82frn64de7balymq9s
        foreign key (category_id) references category (category_id)
);

create table cart
(
    cart_id          bigint auto_increment
        primary key,
    created_by       varchar(255) null,
    created_datetime datetime(6)  null,
    updated_by       varchar(255) null,
    updated_datetime datetime(6)  null,
    quantity         int          null,
    customer_id      bigint       null,
    product_id       bigint       null,
    constraint FK3d704slv66tw6x5hmbm6p2x3u
        foreign key (product_id) references product (product_id),
    constraint FKdebwvad6pp1ekiqy5jtixqbaj
        foreign key (customer_id) references customer (customer_id)
);

create table order_item
(
    order_item_id    bigint auto_increment
        primary key,
    created_by       varchar(255)   null,
    created_datetime datetime(6)    null,
    updated_by       varchar(255)   null,
    updated_datetime datetime(6)    null,
    discount         decimal(38, 2) null,
    price            decimal(38, 2) null,
    quantity         int            null,
    order_id         bigint         null,
    product_id       bigint         null,
    constraint FK551losx9j75ss5d6bfsqvijna
        foreign key (product_id) references product (product_id),
    constraint FKgv4bnmo7cbib2nh0b2rw9yvir
        foreign key (order_id) references customer_order (order_id)
);

create table resource
(
    resource_id      bigint auto_increment
        primary key,
    created_by       varchar(255)  null,
    created_datetime datetime(6)   null,
    updated_by       varchar(255)  null,
    updated_datetime datetime(6)   null,
    description      varchar(2000) null,
    resource_code    varchar(255)  not null
);

create table resource_permission_map
(
    id               bigint auto_increment
        primary key,
    created_by       varchar(255)     null,
    created_datetime datetime(6)      null,
    updated_by       varchar(255)     null,
    updated_datetime datetime(6)      null,
    can_delete       bit default '0' null,
    can_update       bit default '0' null,
    can_view         bit default '0' null,
    permission_id    bigint           null,
    resource_id      bigint           null,
    constraint FK16t5btwuf9hln1vd4gwox0vhx
        foreign key (resource_id) references resource (resource_id),
    constraint FKq35r389bg15otlw3fu8ssg4t8
        foreign key (permission_id) references permission (permission_id)
);

create table shipment
(
    shipment_id      bigint auto_increment
        primary key,
    created_by       varchar(255) null,
    created_datetime datetime(6)  null,
    updated_by       varchar(255) null,
    updated_datetime datetime(6)  null,
    address          varchar(255) null,
    city             varchar(255) null,
    country          varchar(255) null,
    shipment_date    datetime(6)  null,
    state            varchar(255) null,
    zip_code         varchar(255) null,
    customer_id      bigint       null,
    order_id         bigint       null,
    constraint FK6v966axnajud3h5y73ag6jr3g
        foreign key (customer_id) references customer (customer_id),
    constraint FKcfedtdufyrf8t5p9tnekn7sge
        foreign key (order_id) references customer_order (order_id)
);

create table wishlist
(
    wishlist_id      bigint auto_increment
        primary key,
    created_by       varchar(255) null,
    created_datetime datetime(6)  null,
    updated_by       varchar(255) null,
    updated_datetime datetime(6)  null,
    customer_id      bigint       null,
    product_id       bigint       null,
    constraint FKb6xak0rjui1rsok8ll7ln59cs
        foreign key (customer_id) references customer (customer_id),
    constraint FKqchevbfw5wq0f4uqacns02rp7
        foreign key (product_id) references product (product_id)
);

