* Dự án ShoppApp nhằm xây dựng website bán hàng với các công nghệ
Frontend: Angular (Phiên bản 16.1.3, Node JS phiên bản 22.1)
Database: MySQL (Base to XAMPP)
Backend: Spring Boot, Spring Security, Oath2, JPA, Hibernate

1. Chức năng chính
- Quản lý tài khoản: tài khoản khách và tài khoản admin
+ Tài khoản khách: Đăng ký, đăng nhập bằng tài khoản mật khẩu, đăng nhập bằng facebook và gmail
+ Tài khoản admin: Đăng nhập
- Quản lý danh mục
- Quản lý sản phẩm
- Quản lý đơn hàng
- Quản lý chi tiết đơn hàng

2. Thiết kế các bảng CSDL
 - users
	id
	fullname
	phone_number
	address
	password
	created_at
	updated_at
	is_active
	date_of_birth
	facebook_account_id
	google_account_id
	role_id
- roles
	id
	name
- social_accounts
	id
	provider
	privider_id
	email
	name
	user_id
- tokens
	id
	token
	token_type
	expiration_date
	revoked
	expired
	user_id
- orders
	id
	user_id
	fullname
	email
	phone_number
	address
	note
	order_date
	status
	total_money
	shipping_address
	shipping_method
	shipping_date
	tracking_number
	payment_method
	active
- categories
	id
	name
- order_details
	id
	order_id
	product_id
	price
	number_of_products
	total_money
	color
- products
	id
	name
	price
	thumbnail
	description
	created_at
	updates_at
	category_id