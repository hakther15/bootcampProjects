BEGIN TRANSACTION;

-- *************************************************************************************************
-- Drop all db objects in the proper order.
-- *************************************************************************************************
DROP TABLE IF EXISTS cart, users, product CASCADE;

-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************

--users (name is pluralized because 'user' is a SQL keyword)
CREATE TABLE users (
	user_id SERIAL PRIMARY KEY,
	username varchar(50) UNIQUE,
	password_hash varchar(200) NOT NULL,
	address varchar(200),
	city varchar(200),
	state_code varchar(2) NOT NULL,
	ZIP varchar(50),
	role varchar(50) NOT NULL
);

CREATE TABLE product (
	product_id SERIAL PRIMARY KEY,
	product_sku varchar(50) NOT NULL,
	name varchar(50) NOT NULL,
	description text NOT NULL,
	price numeric NOT NULL,
	image_name varchar(150) NOT NULL
);

CREATE TABLE cart (
	cart_id serial PRIMARY KEY,
	user_id int NOT NULL,
	product_id int NOT NULL,
	quantity int NOT NULL,
	CONSTRAINT FK_cart_user FOREIGN KEY (user_id) REFERENCES users (user_id),
	CONSTRAINT FK_cart_product FOREIGN KEY (product_id) REFERENCES product (product_id)
);

INSERT INTO product (product_sku, name, description, price, image_name)
	VALUES
	('BOOK-24', 'Travel Tips and Tricks Guidebook', 'Learn the secrets to stress-free traveling!', 19.99, 'BOOK-24.jpeg'),
	('LTAG-32', 'Luggage Tag - set of 2', 'Unique and fun tags for your exciting travels', 6.99, 'LTAG-32.jpeg'),
	('SMAP-15', 'Scratch Map', 'Keep track of all the places you go!', 12.99, 'SMAP-15.jpeg'),
	('PLSH-63', 'Globe Plush Pillow', 'Soft and cozy - and a pillow!', 15.99, 'PLSH-63.jpeg'),
	('TMBL-09', 'Travel Tumbler', 'Keep your drinks cold or hot when you travel', 29.99, 'TMBL-09.jpeg');


-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************

-- Users
-- Password for all users is password
INSERT INTO
    users (username, password_hash, address, city, state_code, ZIP, role)
VALUES
    ('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', '555 Main Street', 'Cleveland', 'OH', '44115', 'ROLE_USER'),
    ('admin','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', '123 Elm Street', 'New York', 'NY', '10001', 'ROLE_ADMIN');

COMMIT TRANSACTION;