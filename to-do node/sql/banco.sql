CREATE DATABASE IF NOT EXISTS todo;
USE todo;

CREATE TABLE IF NOT EXISTS USER(
    ID int auto_increment primary key,
    USER_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    EMAIL varchar(50) not null unique,
    USER_PASSWORD varchar(100) not null,
    ACTIVATION_CODE varchar(20),
    NEW_PASSWORD_CODE varchar(20),
    USER_STATUS INT NOT NULL,
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS LIST(
    ID int auto_increment primary key,
    LIST_NAME varchar(50) not null,
    LIST_DESCRIPTION TEXT,
    ID_USER int not null,
    FOREIGN KEY (ID_USER)
    REFERENCES USER(ID)
    ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS TASK(
    ID int auto_increment primary key,
    TASK_NAME varchar(50) not null,
    TASK_DESCRIPTION TEXT,
    DEADLINE timestamp,
    TASK_STATUS INT NOT NULL,
    ID_USER int not null,
    FOREIGN KEY (ID_USER)
    REFERENCES USER(ID)
    ON DELETE CASCADE,
    ID_LIST int,
    FOREIGN KEY (ID_LIST)
    REFERENCES LIST(ID)
    ON DELETE CASCADE
) ENGINE=INNODB;
