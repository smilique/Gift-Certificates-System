use gift_certificates_dev;

insert into tag set id = 1, name = 'GYM';
insert into tag set id = 2, name = 'Books';
insert into tag set id = 3, name = 'Grocery';
insert into tag set id = 4, name = 'Electronics';
insert into tag set id = 5, name = 'Swimming pool';
insert into tag set id = 6, name = 'Lasertag';
insert into tag set id = 7, name = 'Billiards';
insert into tag set id = 8, name = 'Jewelry';
insert into tag set id = 9, name = 'Cinema';
insert into tag set id = 10, name = 'Auto Shop';
insert into tag set id = 11, name = 'Clothes';

insert into gift_certificate set id = 1, name = 'Workout Gift Card',description = 'Unlimited access to GYM',
                                 price = 1300, duration = 30,
                                 create_date = '2018-08-29T06:12:15.156', last_update_date = '2018-08-29T06:12:15.156';
insert into gift_certificate set id = 2, name = 'Mall Gift Card', description = 'You can choose any shop in the mall',
                                 price = 2000, duration = 90,
                                 create_date = '2018-09-29T06:12:15.156', last_update_date = '2018-09-29T06:12:15.156';
insert into gift_certificate set id = 3, name = 'Auto Parts', description = 'Auto parts gift card',
                                 price = 1000, duration = 90,
                                 create_date = '2018-10-29T06:12:15.156', last_update_date = '2018-10-29T06:12:15.156';
insert into gift_certificate set id = 4, name = 'Entertainment Centre', description = 'Entertainment centre gift card',
                                 price = 1500, duration = 30,
                                 create_date = '2018-11-29T06:12:15.156', last_update_date = '2018-11-29T06:12:15.156';
insert into gift_certificate set id = 5, name = 'Supermarket Gift Card', description = 'Supermarket gift card',
                                 price = 3000, duration = 180,
                                 create_date = '2018-12-29T06:12:15.156', last_update_date = '2018-12-29T06:12:15.156';

insert into tagged_gift_certificate set gift_certificate_id = 1, tag_id = 1;
insert into tagged_gift_certificate set gift_certificate_id = 1, tag_id = 5;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 2;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 4;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 8;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 9;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 11;
insert into tagged_gift_certificate set gift_certificate_id = 3, tag_id = 10;
insert into tagged_gift_certificate set gift_certificate_id = 4, tag_id = 6;
insert into tagged_gift_certificate set gift_certificate_id = 4, tag_id = 7;
insert into tagged_gift_certificate set gift_certificate_id = 4, tag_id = 9;
insert into tagged_gift_certificate set gift_certificate_id = 5, tag_id = 3;
insert into tagged_gift_certificate set gift_certificate_id = 5, tag_id = 4;
insert into tagged_gift_certificate set gift_certificate_id = 5, tag_id = 11;

insert into role set type = 'admin';
insert into role set type = 'user';
insert into role set type = 'guest';

insert into user set login = 'admin', password = md5('admin'), name = 'Admin', balance = 1000, role_id = 1;
insert into user set login = 'user', password = md5('user'), name = 'User', balance = 0, role_id = 2;
insert into user set login = 'guest', password = md5('guest'), name = 'User', balance = 0, role_id = 3;

insert into orders set cost = 545, certificate_id = 4, user_id = 1, date = '2020-12-29T11:12:15.001';
insert into orders set cost = 300, certificate_id = 3, user_id = 1, date = '2020-12-29T07:12:15.010';
insert into orders set cost = 700, certificate_id = 5, user_id = 1, date = '2020-12-29T05:12:15.100';
insert into orders set cost = 100, certificate_id = 1, user_id = 2, date = '2020-12-01T08:12:15.200';
