
-- this script create the database is none exist

drop table resource_master if exists;

create table resource_master (
	res_id bigint identity not null primary key,
	first_name varchar (100),
	last_name varchar (100),
	designation varchar (100)
);

insert into resource_master 
(res_id, first_name, last_name, designation)
values (1, 'foo', 'bar', 'employee');

insert into resource_master 
(res_id, first_name, last_name, designation)
values (2, 'yolo', 'yolo', 'employee');

insert into resource_master 
(res_id, first_name, last_name, designation)
values (3, 'why', 'me', 'employee');