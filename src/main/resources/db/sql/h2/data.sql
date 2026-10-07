-- ********************************************************************************
-- Created new script by SUVINY on 26. 10. 6.
-- This script file is for defining 'Delfeno-App' DDL statements in H2 In-Memory-DB
-- ********************************************************************************

-- DML section...


-- # 상위 업무 그룹 테이블 사전 데이터 추가
INSERT INTO tasks (
    name,
    sort_no,
    updated_at
) VALUES (
    '요구사항 분석 및 기획',
    1,
    NOW()
);

INSERT INTO tasks (
    name,
    sort_no,
    updated_at
) VALUES (
    '설계',
    2,
    NOW()
);

INSERT INTO tasks (
    name,
    sort_no,
    updated_at
) VALUES (
    '구현',
    3,
    NOW()
);

INSERT INTO tasks (
    name,
    sort_no,
    updated_at
) VALUES (
    '테스트',
    4,
    NOW()
);