-- MySQL 버전
CREATE DATABASE WEATHER DEFAULT CHARSET=utf8 collate=utf8_bin;

USE WEATHER;

-- USER
CREATE TABLE USERLIST(
    User_id VARCHAR(15) UNIQUE NOT NULL,
    PW VARCHAR(15) NOT NULL,
    Uniq_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Nickname VARCHAR(15) NOT NULL,
    Introduction VARCHAR(50),
    Available VARCHAR(1) DEFAULT 'Y' NOT NULL
);

-- REGION
CREATE TABLE REGION(
    Uuid BIGINT NOT NULL,
    Address VARCHAR(50) NOT NULL,
    Latitude DECIMAL(18,14) NOT NULL,
    Longitude DECIMAL(18,14) NOT NULL,
    X_coor INT NOT NULL,
    Y_coor INT NOT NULL,
    PRIMARY KEY(Uuid, Address)
);
ALTER TABLE REGION ADD FOREIGN KEY(Uuid) REFERENCES USERLIST(Uniq_id) ON DELETE CASCADE;

-- WEATEHR_RECORD
CREATE TABLE WEATHER_RECORD(
    Uuid BIGINT NOT NULL,
    Rdate DATE NOT NULL,
    Address VARCHAR(50),
    Feeling VARCHAR(15) NOT NULL,
    Tmax DECIMAL(3,1),
    Tmin DECIMAL(3,1),
    PRIMARY KEY(Uuid,Rdate)
);
ALTER TABLE WEATHER_RECORD ADD FOREIGN KEY(Uuid) REFERENCES USERLIST(Uniq_id) ON DELETE CASCADE;

COMMIT;
