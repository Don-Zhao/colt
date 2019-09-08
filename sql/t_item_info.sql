create table t_item_info(
	id int auto_increment primary key,
	name varchar(50) not null,
	description varchar(50) not null,
	price decimal(10,2) not null,
	img varchar(255)
)