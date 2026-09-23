# 페이징 처리 - MySQL
# LIMIT 인덱스, 개수
# ✨ 데이터의 인덱스 부터 지정한 개수만큼 조회

-- 1페이지
SELECT *
FROM product
LIMIT 0, 10
;

-- 2페이지
SELECT *
FROM product
LIMIT 10, 10 
;
