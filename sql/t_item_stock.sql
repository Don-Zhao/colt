create table t_item_stock (
	id int auto_increment primary key,
	item_id int not null,
	stock int,
	sales int
)