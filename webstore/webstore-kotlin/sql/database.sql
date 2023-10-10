CREATE DATABASE IF NOT EXISTS webstore;
USE webstore;

CREATE USER 'webstore'@'localhost' IDENTIFIED BY 'Webstore#123';
GRANT INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on *.* TO 'webstore'@'localhost' WITH GRANT OPTION;

CREATE TABLE IF NOT EXISTS USER_WEBSTORE(
    ID int auto_increment primary key,
    FIRST_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    EMAIL varchar(50) not null unique,
    USER_PASSWORD varchar(100) not null,
    ACTIVATION_CODE varchar(20),
    NEW_PASSWORD_CODE varchar(20),
    USER_ROLES TEXT NOT NULL,
    USER_STATUS INT NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS CATEGORY(
    ID int auto_increment primary key,
    NAME varchar(50) not null,
    ID_UPPER_CATEGORY int,
        FOREIGN KEY (ID_UPPER_CATEGORY)
        REFERENCES CATEGORY(ID)
        ON DELETE CASCADE
) ENGINE=INNODB;
