CREATE DATABASE ShopApp;
USE ShopApp;
-- Table Users
CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '',
    created_at datetime,
    updated_at datetime,
    is_active tinyint(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0
);
ALTER Table users add column role_id INT;

CREATE Table roles(
    id INT PRIMARY KEY,
    name varchar(20) not null
);

INSERT INTO roles(name)
VALUES ("user")

ALTER Table users add foreign KEY (role_id) references roles(id);

CREATE TABLE tokens(
    id INT PRIMARY KEY AUTO_INCREMENT,
    token varchar(255) unique not null,
    token_type varchar(50) not null,
    expiration_date datetime,
    revoked tinyint(1) not null,
    expired tinyint(1) not null,
    user_id int,
    foreign KEY (user_id) references users(id)
);

-- login google and facebook
CREATE TABLE social_accounts(
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(20) not null COMMENT 'Tên nhà social Network',
    privider_id varchar(50) not null,
    email varchar(150) not null COMMENT 'Email tài khoản',
    name VARCHAR(100) not null COMMENT 'Tên người dùng',
    user_id int,
    foreign KEY (user_id) references users(id)
);

--Danh muc
CREATE TABLE categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null DEFAULT '' COMMENT 'Tên danh mục, vd: Đồ điện tử'
);

--San pham
CREATE TABLE products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(350) COMMENT 'Tên sản phẩm',
    price FLOAT not null check(price >= 0),
    thumbnail varchar(300) DEFAULT '',
    description LONGTEXT DEFAULT '',
    created_at datetime,
    updated_at datetime,
    category_id INT,
    foreign KEY (category_id) references categories(id)
);

--anh san pham
CREATE Table product_images(
    id INT PRIMARY key AUTO_INCREMENT,
    product_id INT,
    foreign KEY (product_id) references products(id),
    constraint fk_product_images_product_id 
        foreign key (product_id) references products (id) on delete cascade,
    image_url VARCHAR(300)

)

--Orders
CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id int,
    foreign KEY (user_id) references users(id),
    fullname varchar(100) DEFAULT '',
    email varchar(100) DEFAULT '',
    phone_number varchar(20) not null,
    address VARCHAR(200) not null,
    note varchar(100) DEFAULT '',
    order_date datetime DEFAULT current_timestamp,
    status varchar(20),
    total_money FLOAT CHECK(total_money >= 0)
);

ALTER TABLE orders add column `shipping_method` varchar(100);
ALTER TABLE orders add column `shipping_address` varchar(200);
ALTER TABLE orders add column `shipping_date` date;
ALTER TABLE orders add column `tracking_number` varchar(100);
ALTER TABLE orders add column `payment_method` varchar(100);

--xoa don hang -> xoa mem -> them truong active
ALTER TABLE orders add column active tinyint(1);

--trang thai don ahng chi dc phep nhan mot gia tri cu the
ALTER Table orders
modify column status enum ('pending', 'processing', 'shipped', 'delivered', 'cancelled')
COMMENT 'Trạng thái đơn hàng'

CREATE TABLE order_details(
    id int PRIMARY key AUTO_INCREMENT,
    order_id int,
    foreign key (order_id) references orders(id),
    product_id int,
    foreign key (product_id) references products (id),
    price FLOAT check(price >= 0),
    number_of_products INT check (number_of_products > 0),
    total_money FLOAT check(total_money >= 0),
    color varchar(20) DEFAULT ''
);
