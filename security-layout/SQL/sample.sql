-- Active: 1788828726528@@127.0.0.1@3306@aloha
-- 사용자 계정
INSERT INTO `user` (id, username, password, name, email)
VALUES
    (UUID(), 'user', '$2a$10$PMkZ5OE8AxstxMJi1RSHtu.Z4W2m7ZGSPTiruR1navWluw9hk6/Pq',
     '일반 사용자', 'user@naver.com'),

    (UUID(), 'aloha', '$2a$10$PMkZ5OE8AxstxMJi1RSHtu.Z4W2m7ZGSPTiruR1navWluw9hk6/Pq',
     '알로하', 'aloha@naver.com');


-- 사용자 권한
INSERT INTO `user_auth` (id, username, auth)
VALUES
    (UUID(), 'user', 'ROLE_USER'),
    (UUID(), 'aloha', 'ROLE_USER');

-- 관리자 계정
INSERT INTO `user` (id, username, password, name, email, enabled)
VALUES (UUID(), 'admin', '$2a$10$PMkZ5OE8AxstxMJi1RSHtu.Z4W2m7ZGSPTiruR1navWluw9hk6/Pq', 
        '관리자', 'admin@naver.com', 1);

INSERT INTO `user_auth` (id, user_no, auth )
VALUES 
  (UUID(), (SELECT no FROM user WHERE username = 'admin'), 'ROLE_USER'),
  (UUID(), (SELECT no FROM user WHERE username = 'admin'), 'ROLE_ADMIN');