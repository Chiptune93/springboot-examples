DROP TABLE IF EXISTS users;

CREATE TABLE users (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      auth_type VARCHAR(255) NOT NULL
);

INSERT INTO users (username, email, password, auth_type) VALUES ('admin', 'user1@example.com', '1234','ADMIN');
INSERT INTO users (username, email, password, auth_type) VALUES ('user', 'user1@example.com', '1234','USER');
INSERT INTO users (username, email, password, auth_type) VALUES ('super', 'user1@example.com', '1234','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user1', 'user1@example.com', 'pass1','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user2', 'user2@example.com', 'pass2','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user3', 'user3@example.com', 'pass3','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user4', 'user4@example.com', 'pass4','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user5', 'user5@example.com', 'pass5','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user6', 'user6@example.com', 'pass6','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user7', 'user7@example.com', 'pass7','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user8', 'user8@example.com', 'pass8','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user9', 'user9@example.com', 'pass9','ALL');
INSERT INTO users (username, email, password, auth_type) VALUES ('user10', 'user10@example.com', 'pass10','ALL');
