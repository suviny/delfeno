-- DDL section


-- 사용자 테이블
drop table if exists users cascade;

create table if not exists users (
    id                  int                 not null auto_increment,
    email               varchar(50)         not null unique,
    name                varchar(10)         not null,
    auth_type           varchar(5)          not null,
    enabled             tinyint             not null default 1,
    deleted_yn          char(1)             not null default 'N',
    created_at          datetime            not null default now(),
    updated_at          datetime            not null,
    deleted_at          datetime            null,
    primary key (id)
);


-- 사용자 로컬 인증 정보 테이블
drop table if exists user_credentials cascade;

create table if not exists user_credentials (
    user_id             int                 not null,
    password            varchar(255)        not null,
    updated_at          datetime            not null,
    primary key (user_id),
    foreign key (user_id) references users (id)
);


-- 사용자 소셜 인증 정보 테이블
drop table if exists user_socials cascade;

create table if not exists user_socials (
    id,
    user_id             int                 not null,
    provider            varchar(50)         not null,
    provider_id         varchar(255)        not null,
    created_at          datetime            not null default now(),
    primary key (id),
    foreign key (user_id) references users (id)
);


-- 사용자별 보유 권한 정보 테이블
drop table if exists user_roles cascade;

create table if not exists user_roles (
    user_id             int                 not null,
    role                varchar(20)         not null,
    primary key (user_id, role),
    foreign key (user_id) references users (id)
);


-- 사용자 프로필 정보 테이블
drop table if exists profiles cascade;

create table if not exists profiles (
    id                  int                 not null auto_increment,
    user_id             int                 not null,
    profile_img_url     varchar(255)        not null,
    nickname            varchar(25)         not null unique,
    bio                 varchar(150)        null,
    univ                varchar(45)         null,
    site_url            varchar(255)        null,
    github              varchar(255)        null,
    updated_at          datetime            not null,
    primary key (id),
    foreign key (user_id) references users (id)
);


-- 로그인 이력 테이블
drop table if exists login_histories cascade;

create table if not exists login_histories (
    id                  int                 not null auto_increment,
    user_id             int                 not null,
    ip_address          varchar(45)         not null,
    device              varchar(25)         not null,
    os                  varchar(20)         not null,
    browser             varchar(20)         not null,
    login_at            datetime            not null,
    primary key (id),
    foreign key (user_id) references users (id)
);