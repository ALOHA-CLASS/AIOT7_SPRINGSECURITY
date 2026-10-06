-- 장바구니 샘플데이터
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE cart_item;

INSERT INTO cart_item
    (id, quantity, product_no, user_no, created_at, updated_at)
VALUES
    (UUID(), 1,  1, 1, NOW(), NOW()),
    (UUID(), 2,  2, 1, NOW(), NOW()),
    (UUID(), 1,  3, 1, NOW(), NOW()),
    (UUID(), 3,  4, 1, NOW(), NOW()),
    (UUID(), 2,  5, 1, NOW(), NOW()),
    (UUID(), 1,  6, 1, NOW(), NOW()),
    (UUID(), 2,  7, 1, NOW(), NOW()),
    (UUID(), 1,  8, 1, NOW(), NOW()),
    (UUID(), 3,  9, 1, NOW(), NOW()),
    (UUID(), 2, 10, 1, NOW(), NOW());