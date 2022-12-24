drop table if exists gift_certificates_dev.orders;
drop table if exists gift_certificates_dev.user;
drop table if exists gift_certificates_dev.tagged_gift_certificate;
drop table if exists gift_certificates_dev.gift_certificate;
drop table if exists gift_certificates_dev.tag;

drop database if exists gift_certificates_dev;

create database gift_certificates_dev;
use gift_certificates_dev;

create table gift_certificate
(
    id bigint auto_increment primary key,
    name varchar(45) null,
    description varchar(255) null,
    price decimal(9,2) null default 0,
    duration bigint null default 0,
    create_date datetime(3),
    last_update_date datetime(3)
)
    charset = utf8;

create table tag
(
    id bigint auto_increment primary key,
    name varchar(45) unique
#     unique key unique_name (name)
)
    charset = utf8;

create table tagged_gift_certificate
(
        id bigint auto_increment primary key,
        gift_certificate_id bigint null,
        constraint certificate
            foreign key (gift_certificate_id) references gift_certificate (id) on delete cascade ,
        tag_id bigint null,
        constraint tag
            foreign key (tag_id) references tag (id) on delete cascade
)
    charset = utf8;

create table user
(
    id bigint auto_increment primary key,
    login varchar(45) null,
    password varchar(45) null,
    name varchar(45) null,
    balance decimal(9,2) null,
    role_id bigint null,
    constraint fk_role_id
        foreign key (role_id) references role (id) on delete cascade
)
    charset = utf8;

create table orders
(
    id bigint auto_increment primary key,
    cost decimal null,
    certificate_id bigint null,
    constraint ordered_certificate
        foreign key (certificate_id) references gift_certificate (id) on delete cascade,
    user_id bigint null,
    constraint ordered_user
        foreign key (user_id) references user (id) on delete cascade,
    date datetime(3)
)
    charset = utf8;

create table role
(
    id bigint auto_increment primary key,
    type varchar(15) null
)
charset = utf8;
