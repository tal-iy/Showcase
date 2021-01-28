create table account
(
	UserID int auto_increment,
	UserName char(255) not null,
	Email char(255) not null,
	Password char(255) not null,
	CardInfo text null,
	BillingAddress text null,
	primary key (UserID, UserName)
);

create table item
(
	ItemID int auto_increment
		primary key,
	ItemName char(255) default 'Item Name' not null,
	ItemDescription text not null,
	ItemPrice double(16,2) not null,
	StockQuantity int default 0 not null
);

create table cart
(
	ItemID int not null,
	UserID int not null,
	ItemQuantity int default 1 not null,
	primary key (ItemID, UserID),
	foreign key (UserID) references account (UserID),
	foreign key (ItemID) references item (ItemID)
);

create table category
(
	CategoryID int auto_increment
		primary key,
	CategoryName char(255) default 'Default Category' not null,
	CategoryDescription text not null
);

create table group_table
(
	CategoryID int not null,
	ItemID int not null,
	GroupRank int not null,
	primary key (CategoryID, ItemID),
	foreign key (CategoryID) references category (CategoryID),
	foreign key (ItemID) references item (ItemID)
);

create table order_table
(
	OrderID int auto_increment
		primary key,
	UserID int not null,
	TotalPrice double(16,2) not null,
	DeliveryAddress text not null,
	OrderDate date not null,
	foreign key (UserID) references account (UserID)
);

create table purchase
(
	OrderID int not null,
	ItemID int not null,
	ItemQuantity int default 1 not null,
	PurchasePrice double(16,2) not null,
	primary key (OrderID, ItemID),
	foreign key (ItemID) references item (ItemID),
	foreign key (OrderID) references order_table (OrderID)
);

create table review
(
	UserID int not null,
	ItemID int not null,
	ReviewRating int default 5 not null,
	ReviewText text not null,
	primary key (UserID, ItemID),
	foreign key (UserID) references account (UserID),
	foreign key (ItemID) references item (ItemID)
);

