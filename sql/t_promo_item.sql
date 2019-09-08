create table t_promo_item(
	id int auto_increment primary key,
	name varchar(50) not null,
	item_id int not null,
	start_time timestamp not null,
	end_time timestamp not null,
	promo_price decimal not null
)