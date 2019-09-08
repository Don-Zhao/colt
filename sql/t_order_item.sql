create table t_order_item(
	id varchar(32) not null,
	user_id int not null,
	order_price DECIMAL not null,
	count int not null,
	flag tinyint(1) default 0,
	oid int auto_increment primary key
)