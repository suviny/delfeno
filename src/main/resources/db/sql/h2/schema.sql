-- ********************************************************************************
-- Created new script by SUVINY on 26. 10. 6.
-- This script file is for defining 'Delfeno-App' DDL statements in H2 In-Memory-DB
-- ********************************************************************************

-- DDL section...


-- # 샘플
--DROP TABLE IF EXISTS 테이블명 CASCADE;
--CREATE TABLE IF EXISTS 테이블명 (
--    column_name,
--    PRIMARY KEY (컬럼명),
--    CONSTRAINT 제약조건명 [UNIQUE | FOREIGN KEY] (컬럼명) [REFERENCES 참조_테이블명 (참조 PK)]
--);


-- # 사용자 계정 테이블
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF EXISTS users (
    id                  INT                 NOT NULL AUTO_INCREMENT,
    email               VARCHAR(50)         NOT NULL,
    nickname            VARCHAR(25)         NOT NULL,
    login_type          VARCHAR(10)         NOT NULL,
    enabled             TINYINT(1)          NOT NULL DEFAULT 1,
    deleted_yn          CHAR(1)             NOT NULL DEFAULT 'N',
    created_at          DATETIME            NOT NULL DEFAULT NOW(),
    updated_at          DATETIME            NOT NULL,
    deleted_at          DATETIME            NULL,
    PRIMARY KEY (id),
    CONSTRAINT USERS_EMAIL_UK UNIQUE (email),
    CONSTRAINT USERS_NICKNAME_UK UNIQUE (nickname)
);


-- # 사용자 권한 매핑 테이블
DROP TABLE IF EXISTS user_roles CASCADE;
CREATE TABLE IF EXISTS user_roles (
    user_id             INT                 NOT NULL,
    role_type           VARCHAR(10)         NOT NULL,
    PRIMARY KEY (user_id, role_type),
    CONSTRAINT USER_ROLES_USER_ID_FK FOREIGN KEY (user_id) REFERENCES users (id)
);


-- # 로컬 계정 비밀번호 테이블
DROP TABLE IF EXISTS auth_credentials CASCADE;
CREATE TABLE IF EXISTS auth_credentials (
    user_id             INT                 NOT NULL,
    password            VARCHAR(255)        NOT NULL,
    updated_at          DATETIME            NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT AUTH_CREDENTIALS_USER_ID_FK FOREIGN KEY (user_id) REFERENCES users (id)
);


-- # 소셜 계정 인증 정보 테이블
DROP TABLE IF EXISTS auth_socials CASCADE;
CREATE TABLE IF EXISTS auth_socials (
    id                  INT                 NOT NULL AUTO_INCREMENT,
    user_id             INT                 NOT NULL,
    provider            VARCHAR(50)         NOT NULL,
    provider_id         VARCHAR(255)        NOT NULL,
    created_at          DATETIME            NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    CONSTRAINT AUTH_SOCIALS_USER_ID_FK FOREIGN KEY (user_id) REFERENCES users (id)
);


-- # 사용자 프로필 정보 테이블
DROP TABLE IF EXISTS profiles CASCADE;
CREATE TABLE IF EXISTS profiles (
    id                  INT                 NOT NULL AUTO_INCREMENT,
    user_id             INT                 NOT NULL,
    profile_img_url     VARCHAR(255)        NULL,
    bio                 VARCHAR(150)        NULL,
    univ                VARCHAR(50)         NULL,
    site_url            VARCHAR(255)        NULL,
    github              VARCHAR(255)        NULL,
    updated_at          DATETIME            NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT PROFILES_USER_ID_FK FOREIGN KEY (user_id) REFERENCES users (id)
);


-- # 로그인 이력 테이블
DROP TABLE IF EXISTS login_histories CASCADE;
CREATE TABLE IF EXISTS login_histories (
    id                  INT                 NOT NULL AUTO_INCREMENT,
    user_id             INT                 NOT NULL,
    ip_address          VARCHAR(45)         NOT NULL,
    device              VARCHAR(20)         NOT NULL,
    os                  VARCHAR(50)         NOT NULL,
    browser             VARCHAR(50)         NOT NULL,
    login_at            DATETIME            NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT LOGIN_HISTORIES_USER_ID_FK FOREIGN KEY (user_id) REFERENCES users (id)
);


-- # 상위 업무 그룹
DROP TABLE IF EXISTS tasks CASCADE;
CREATE TABLE IF EXISTS tasks (
    id                  INT                 NOT NULL AUTO_INCREMENT,
    name                VARCHAR(50)         NOT NULL,
    used_yn             CHAR(1)             NOT NULL DEFAULT 'Y',
    sort_no             INT                 NOT NULL DEFAULT 0,
    created_at          DATETIME            NOT NULL DEFAULT NOW(),
    updated_at          DATETIME            NOT NULL,
    PRIMARY KEY (id)
);