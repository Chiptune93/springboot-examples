DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'pass1');
INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'pass2');
INSERT INTO users (username, email, password) VALUES ('user3', 'user3@example.com', 'pass3');
INSERT INTO users (username, email, password) VALUES ('user4', 'user4@example.com', 'pass4');
INSERT INTO users (username, email, password) VALUES ('user5', 'user5@example.com', 'pass5');
INSERT INTO users (username, email, password) VALUES ('user6', 'user6@example.com', 'pass6');
INSERT INTO users (username, email, password) VALUES ('user7', 'user7@example.com', 'pass7');
INSERT INTO users (username, email, password) VALUES ('user8', 'user8@example.com', 'pass8');
INSERT INTO users (username, email, password) VALUES ('user9', 'user9@example.com', 'pass9');
INSERT INTO users (username, email, password) VALUES ('user10', 'user10@example.com', 'pass10');
