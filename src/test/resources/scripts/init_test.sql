drop table if exists tagged_gift_certificate;
drop table if exists gift_certificate;
drop table if exists tag;

create table gift_certificate
(
    id bigint auto_increment primary key,
    name varchar(45) null,
    description varchar(255) null,
    price decimal(9,2) null default 0,
    duration bigint null default 0,
    create_date datetime(3),
    last_update_date datetime(3)
);

create table tag
(
    id bigint auto_increment primary key,
    name varchar(45) null
);


create table tagged_gift_certificate
(
        id bigint auto_increment primary key,
        gift_certificate_id bigint null,
        constraint certificate
            foreign key (gift_certificate_id) references gift_certificate (id) on delete cascade,
        tag_id bigint null,
        constraint tag
            foreign key (tag_id) references tag (id) on delete cascade
);

