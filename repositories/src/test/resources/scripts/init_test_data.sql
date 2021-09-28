insert into tag set id = 1, name = 'Test tag 1';
insert into tag set id = 2, name = 'Test tag 2';
insert into tag set id = 3, name = 'Test tag 3';
insert into tag set id = 4, name = 'Test tag 4';


insert into gift_certificate set id = 1, name = 'B# Test certificate 1',description = 'Certificate 1 description',
                                 price = 1300, duration = 30,
                                 create_date = '2018-08-29T03:12:15.100Z', last_update_date = '2018-08-29T03:12:15.100Z';
insert into gift_certificate set id = 2, name = 'A# Test certificate 2', description = 'Certificate 2 description',
                                 price = 2000, duration = 90,
                                 create_date = '2018-10-29T03:12:15.200Z', last_update_date = '2018-10-29T03:12:15.200Z';
insert into gift_certificate set id = 3, name = 'C Test certificate 3', description = 'Certificate 3 description',
                                 price = 1000, duration = 90,
                                 create_date = '2018-09-29T03:12:15.200Z', last_update_date = '2018-09-29T03:12:15.200Z';


insert into tagged_gift_certificate set gift_certificate_id = 1, tag_id = 1;
insert into tagged_gift_certificate set gift_certificate_id = 1, tag_id = 4;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 2;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 4;
insert into tagged_gift_certificate set gift_certificate_id = 2, tag_id = 3;
insert into tagged_gift_certificate set gift_certificate_id = 3, tag_id = 3;
insert into tagged_gift_certificate set gift_certificate_id = 3, tag_id = 1;



