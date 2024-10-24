BEGIN TRANSACTION;

-- Users
INSERT INTO users (username, password_hash, address, city, state, zip_code, role) VALUES ('user1','user1', '555 Main Street', 'Cleveland', 'OH', '44115', 'ROLE_USER');
INSERT INTO users (username, password_hash, address, city, state, zip_code, role) VALUES ('user2','user2', '123 Elm Street', 'New York', 'NY', '10001', 'ROLE_USER');
INSERT INTO users (username, password_hash, address, city, state, zip_code, role) VALUES ('user3','user3', '101 33rd Avenue', 'Ann Arbor', 'MI', '48103', 'ROLE_USER');

COMMIT TRANSACTION;
