create table t_cart_item(
	id int auto_increment primary key,
	item_id int not null,
	price decimal not null,
	count int not null,
	cart_id int not null,
	order_flag tinyint not null
)