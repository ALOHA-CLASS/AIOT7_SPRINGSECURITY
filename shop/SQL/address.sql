
TRUNCATE TABLE address;
INSERT INTO address
    (user_no, name, receiver, phone, zipcode, address1, address2, default_address)
VALUES
    (1, '우리집', '한성호', '010-1234-5678', '21999',
     '인천광역시 연수구 송도동',
     '101동 1001호',
     true),

    (1, '사무실', '한성호', '010-1234-5678', '34100',
     '대전광역시 유성구 대학로 123',
     '5층',
     false),

    (1, '강의실', '한성호', '010-1234-5678', '34100',
     '대전광역시 유성구 테크노중앙로 456',
     '302호',
     false),

    (1, '아지트', '한성호', '010-1234-5678', '34100',
     '대전광역시 유성구 봉명동 789',
     '2층',
     false);