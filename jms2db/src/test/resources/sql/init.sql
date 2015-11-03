create table customer (id varchar(15), name varchar(10));
insert into customer values('cust1','jstrachan');
insert into customer values('cust2','nsandhu');
insert into customer values('cust3','willem');

create table tableWithAutoIncr (id int not null GENERATED ALWAYS AS IDENTITY, content varchar(10));
insert into tableWithAutoIncr (content) values ('value1');
