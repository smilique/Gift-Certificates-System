use gift_certificates_prod;

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

insert into gift_certificate set id = 1, name = 'PowerGYM Workout Centre',
                                 description = 'Unlimited access to GYM of your choice (including swimming pool)',
                                 price = 1300, duration = 30,
                                 create_date = '2018-08-29T06:12:15.156', last_update_date = '2018-08-29T06:12:15.156';
insert into gift_certificate set id = 2, name = 'Galaxy Mall Gift Card', description = 'You can choose any shop in the Galaxy mall',
                                 price = 2000, duration = 90,
                                 create_date = '2018-09-29T06:12:15.156', last_update_date = '2018-09-29T06:12:15.156';
insert into gift_certificate set id = 3, name = 'DX Auto Parts', description = '', price = 1000, duration = 90,
                                 create_date = '2018-10-29T06:12:15.156', last_update_date = '2018-10-29T06:12:15.156';
insert into gift_certificate set id = 4, name = 'Cosmos Entertainment Centre',
                                 description = 'Billiards, darts, bowling, pool and mini golf included', price = 1500, duration = 30,
                                 create_date = '2018-11-29T06:12:15.156', last_update_date = '2018-11-29T06:12:15.156';
insert into gift_certificate set id = 5, name = 'Riverside Supermarket Gift Card',
                                 description = 'You can use this gift card for payments in for everything except alcohol and tobacco',
                                 price = 3000, duration = 180,
                                 create_date = '2020-12-29T06:12:15.156', last_update_date = '2020-12-29T06:12:15.156';

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



