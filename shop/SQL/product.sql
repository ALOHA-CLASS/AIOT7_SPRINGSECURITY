SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE product;

INSERT INTO product
    (id, name, category, price, stock, description, image_url, created_at, updated_at)
VALUES
    (
        UUID(),
        '나이키 프리 런',
        '신발',
        129000,
        50,
        '가볍고 편안한 러닝화',
        'https://images.unsplash.com/photo-1552346154-21d32810aba3?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '데일리 백팩',
        '가방',
        89000,
        30,
        '심플한 데일리 백팩',
        'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '미니멀 워치',
        '시계',
        159000,
        20,
        '미니멀 디자인 손목시계',
        'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '무선 헤드폰',
        '음향',
        199000,
        40,
        '노이즈 캔슬링 헤드폰',
        'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '라운드 선글라스',
        '액세서리',
        79000,
        60,
        '클래식 라운드 선글라스',
        'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '레더 자켓',
        '의류',
        259000,
        15,
        '빈티지 레더 자켓',
        'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '폴라로이드 카메라',
        '가전',
        149000,
        25,
        '즉석 인화 카메라',
        'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '향수 No.5',
        '뷰티',
        189000,
        35,
        '시그니처 향수',
        'https://images.unsplash.com/photo-1541643600914-78b084683601?w=600',
        NOW(),
        NOW()
    );


-- 샘플 데이터 추가 --
INSERT INTO product
    (id, name, category, price, stock, description, image_url, created_at, updated_at)
VALUES
    (
        UUID(),
        '오버핏 후드티',
        '의류',
        69000,
        45,
        '편안한 오버핏 데일리 후드티',
        'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '스니커즈 클래식',
        '신발',
        119000,
        35,
        '깔끔한 디자인의 클래식 스니커즈',
        'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '캔버스 토트백',
        '가방',
        49000,
        55,
        '가볍게 들기 좋은 캔버스 토트백',
        'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '데님 팬츠',
        '의류',
        79000,
        40,
        '어떤 스타일에도 잘 어울리는 데님 팬츠',
        'https://images.unsplash.com/photo-1542272604-787c3835535d?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '니트 스웨터',
        '의류',
        85000,
        30,
        '부드럽고 따뜻한 데일리 니트',
        'https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '스마트 워치',
        '전자기기',
        329000,
        18,
        '건강 관리와 운동 기록을 지원하는 스마트 워치',
        'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '블루투스 스피커',
        '음향',
        129000,
        28,
        '휴대하기 좋은 고음질 블루투스 스피커',
        'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '기계식 키보드',
        '전자기기',
        149000,
        22,
        '타건감이 뛰어난 기계식 키보드',
        'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '무선 마우스',
        '전자기기',
        59000,
        50,
        '깔끔하고 편안한 무선 마우스',
        'https://images.unsplash.com/photo-1527814050087-3793815479db?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '스테인리스 텀블러',
        '생활용품',
        39000,
        70,
        '보온과 보냉이 가능한 스테인리스 텀블러',
        'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '데스크 램프',
        '생활용품',
        65000,
        32,
        '심플한 디자인의 데스크용 조명',
        'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '미니 화분',
        '생활용품',
        25000,
        80,
        '책상 위에 놓기 좋은 미니 식물 화분',
        'https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '가죽 지갑',
        '액세서리',
        99000,
        25,
        '깔끔한 디자인의 천연 가죽 지갑',
        'https://images.unsplash.com/photo-1627123424574-724758594e93?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '메탈 안경',
        '액세서리',
        89000,
        35,
        '심플한 메탈 프레임 안경',
        'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '실버 목걸이',
        '액세서리',
        129000,
        20,
        '데일리로 착용하기 좋은 실버 목걸이',
        'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '베이직 셔츠',
        '의류',
        59000,
        60,
        '깔끔하고 활용도가 높은 베이직 셔츠',
        'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '러닝 쇼츠',
        '의류',
        49000,
        45,
        '가볍고 통기성이 좋은 러닝 쇼츠',
        'https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '요가 매트',
        '스포츠',
        55000,
        40,
        '홈트레이닝과 요가에 적합한 요가 매트',
        'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '러닝 백팩',
        '스포츠',
        79000,
        27,
        '운동과 일상에서 활용하기 좋은 러닝 백팩',
        'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '디지털 카메라',
        '전자기기',
        459000,
        12,
        '일상을 선명하게 기록하는 디지털 카메라',
        'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=600',
        NOW(),
        NOW()
    );

-- 샘플 데이터 추가 --
INSERT INTO product
    (id, name, category, price, stock, description, image_url, created_at, updated_at)
VALUES
    (
        UUID(),
        '에어팟 프로',
        '전자기기',
        359000,
        25,
        '몰입감 높은 무선 노이즈 캔슬링 이어폰',
        'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '노트북 파우치',
        '가방',
        39000,
        50,
        '노트북을 안전하게 보호하는 심플한 파우치',
        'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '크로스백',
        '가방',
        69000,
        42,
        '가볍게 착용할 수 있는 데일리 크로스백',
        'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '울 코트',
        '의류',
        239000,
        16,
        '겨울철 따뜻하게 입기 좋은 울 코트',
        'https://images.unsplash.com/photo-1539533113208-f6df8cc8b543?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '플리스 자켓',
        '의류',
        109000,
        28,
        '부드럽고 따뜻한 데일리 플리스 자켓',
        'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '카고 팬츠',
        '의류',
        89000,
        36,
        '실용적인 포켓 디자인의 카고 팬츠',
        'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '러닝화 에어',
        '신발',
        179000,
        32,
        '쿠셔닝이 뛰어난 데일리 러닝화',
        'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '하이탑 스니커즈',
        '신발',
        99000,
        24,
        '캐주얼 스타일에 어울리는 하이탑 스니커즈',
        'https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '샌들 클래식',
        '신발',
        59000,
        45,
        '여름철 편안하게 신기 좋은 클래식 샌들',
        'https://images.unsplash.com/photo-1603487742131-4160ec999306?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '가죽 벨트',
        '액세서리',
        49000,
        38,
        '정장과 캐주얼 모두 활용 가능한 가죽 벨트',
        'https://images.unsplash.com/photo-1624222247344-550fb60583dc?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '메탈 팔찌',
        '액세서리',
        79000,
        26,
        '심플한 메탈 소재의 데일리 팔찌',
        'https://images.unsplash.com/photo-1611652022419-a9419f74343d?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '클래식 반지',
        '액세서리',
        69000,
        30,
        '깔끔한 디자인의 클래식 반지',
        'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '스킨케어 세트',
        '뷰티',
        89000,
        35,
        '데일리 피부 관리를 위한 스킨케어 세트',
        'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '아로마 캔들',
        '뷰티',
        35000,
        55,
        '은은한 향으로 공간을 채워주는 아로마 캔들',
        'https://images.unsplash.com/photo-1603006905003-be475563bc59?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '향수 오 드 퍼퓸',
        '뷰티',
        159000,
        22,
        '은은하고 세련된 향의 오 드 퍼퓸',
        'https://images.unsplash.com/photo-1594035910387-fea47794261f?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '스마트 전구',
        '생활용품',
        29000,
        65,
        '스마트폰으로 조절할 수 있는 LED 스마트 전구',
        'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '무드등',
        '생활용품',
        45000,
        48,
        '침실과 책상에 어울리는 감성 무드등',
        'https://images.unsplash.com/photo-1540932239986-30128078f3c5?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '원목 트레이',
        '생활용품',
        32000,
        40,
        '주방과 거실에서 활용하기 좋은 원목 트레이',
        'https://images.unsplash.com/photo-1603199506016-b9a594b593c0?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '캠핑 의자',
        '스포츠',
        79000,
        34,
        '휴대와 보관이 편리한 접이식 캠핑 의자',
        'https://images.unsplash.com/photo-1478131143081-80f7f84ca84d?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '등산 백팩',
        '스포츠',
        129000,
        21,
        '등산과 여행에 적합한 기능성 백팩',
        'https://images.unsplash.com/photo-1551632811-561732d1e306?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '필름 카메라',
        '가전',
        189000,
        14,
        '감성적인 사진을 촬영할 수 있는 필름 카메라',
        'https://images.unsplash.com/photo-1452780212940-6f5c0d14d848?w=600',
        NOW(),
        NOW()
    ),
    (
        UUID(),
        '커피 드립 세트',
        '주방용품',
        69000,
        33,
        '홈카페를 위한 커피 드립 세트',
        'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600',
        NOW(),
        NOW()
    );