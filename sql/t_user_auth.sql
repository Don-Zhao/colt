create table t_user_auth (
	id int auto_increment primary key,
	user_id int not null,
	password varchar(64) not null,
	captcha varchar(5)
);
