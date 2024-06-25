INSERT INTO test_user (user_id, user_pw, user_type, user_name, user_email)
VALUES ('user1', 'user1', 'user', '사용자1', 'user1@example.com'),
       ('user2', 'user2', 'user', '사용자2', 'user2@example.com'),
       ('admin1', 'admin1', 'admin', '관리자1', 'admin1@example.com'),
       ('admin2', 'admin2', 'admin', '관리자2', 'admin2@example.com');

INSERT INTO test_user_role (user_id, role_name)
VALUES ('user1', '일반사용자'),
       ('user2', '개념사용자'),
       ('admin1', '일반관리자'),
       ('admin2', '대표관리자');

INSERT INTO test_user_authlist (user_id, auth_name)
VALUES ('user1', 'ACCESS'),
       ('user1', 'CREATE'),
       ('user1', 'READ'),
       ('user1', 'DELETE'),
       ('user1', 'UPDATE'),
       ('user2', 'ACCESS'),
       ('user2', 'READ'),
       ('admin1', 'ACCESS'),
       ('admin2', 'ACCESS'),
       ('admin2', 'CREATE'),
       ('admin2', 'READ'),
       ('admin2', 'DELETE'),
       ('admin2', 'UPDATE');
