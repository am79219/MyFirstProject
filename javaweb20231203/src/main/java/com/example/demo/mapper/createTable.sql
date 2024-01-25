drop table if exists cartItem;
drop table if exists cart;
drop table if exists user;
drop table if exists product;


-- 建立 User
create table if not exists user(
	userId int auto_increment primary key,
	name varchar(25) not null,
	username varchar(25) not null unique,
	password varchar(25) not null,
	email varchar(25),
	level int
);

-- 設置 AUTO_INCREMENT = 101
alter table user auto_increment=101;

-- 建立 Product
create table if not exists product(
	productId int auto_increment primary key,
	productName varchar(25) not null unique,
	price int,
	isLaunch boolean,
    category int,
	stock int,
	picName varchar(50)
);

-- 設置 AUTO_INCREMENT = 4001
alter table product auto_increment=4001;

-- 建立Cart
create table if not exists cart(
	cartId int auto_increment primary key,
	cartNo varchar(25),
	userId int not null,
	isCheckout boolean,
	checkoutTime datetime,
	isShipping boolean,
	shippingTime datetime,
	isArrived boolean,
	arrivedTime datetime
);

-- 設置 AUTO_INCREMENT = 8001
alter table cart auto_increment=8001;

-- 建立 CartItem
create table if not exists cartItem(
	cartItemId int auto_increment primary key,
	cartId int,
	productId int not null,
	quantity int not null,
    foreign key (productId) references product(productId)
);

-- 設置 AUTO_INCREMENT = 1
alter table cartItem auto_increment=1;

-- 預設資料
insert into user(name, username, password, email, level)
values('Mandy','am79','1234','am79@abc.com',2);
insert into user(name, username, password, email, level)
values('Shin','db1212','5678','db1202@aaa.com',1);
insert into user(name, username, password, email, level)
values('Chen','sam','0000','db1007@aaa.com',1);

insert into product(productName,price,isLaunch,category,stock,picName)
values('玉米',20,true,1,50,'1玉米.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('花椰菜',40,false,1,0,'1花椰菜.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('青椒',30,true,1,50,'1青椒.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('紅蘿蔔',30,true,1,50,'1紅蘿蔔.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('西瓜',100,true,2,50,'2西瓜.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('鳳梨',150,true,2,50,'2鳳梨.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('杏仁',120,true,3,50,'3杏仁.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('紅豆',100,true,3,50,'3紅豆.jpg');
insert into product(productName,price,isLaunch,category,stock,picName)
values('黃豆',130,true,3,50,'3黃豆.jpg');

insert into cart(cartNo, userId, isCheckout, isShipping, isArrived)
values('20231211_101_8001', 101, false, false, false);
insert into cart(cartNo, userId, isCheckout, checkoutTime, isShipping, isArrived)
values('20231211_102_8002', 102, true, 20231212, false, false);

insert into cartItem(cartId,productId,quantity)
values(8001,4001,5);
insert into cartItem(cartId,productId,quantity)
values(8001,4002,3);
insert into cartItem(cartId,productId,quantity)
values(8002,4002,10);