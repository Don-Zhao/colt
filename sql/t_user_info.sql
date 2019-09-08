create table t_user_info (
	id int auto_increment primary key,
	name varchar(64),
	email varchar(64) not null,
	age int
)