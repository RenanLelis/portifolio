CREATE DATABASE IF NOT EXISTS todo;
USE todo;

CREATE USER 'todo'@'localhost' IDENTIFIED BY 'Todo#123';
GRANT INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on *.* TO 'todo'@'localhost' WITH GRANT OPTION;

CREATE TABLE IF NOT EXISTS USER_TODO(
    ID int auto_increment primary key,
    FIRST_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    EMAIL varchar(50) not null unique,
    USER_PASSWORD varchar(100) not null,
    ACTIVATION_CODE varchar(20),
    NEW_PASSWORD_CODE varchar(20),
    USER_STATUS INT NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS TASK_LIST(
    ID int auto_increment primary key,
    LIST_NAME varchar(50) not null,
    LIST_DESCRIPTION TEXT,
    ID_USER int not null,
    FOREIGN KEY (ID_USER)
    REFERENCES USER_TODO(ID)
    ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS TASK(
    ID int auto_increment primary key,
    TASK_NAME varchar(50) not null,
    TASK_DESCRIPTION TEXT,
    DEADLINE varchar(12),
    TASK_STATUS INT NOT NULL,
    ID_USER int not null,
    FOREIGN KEY (ID_USER)
    REFERENCES USER_TODO(ID)
    ON DELETE CASCADE,
    ID_LIST int,
    FOREIGN KEY (ID_LIST)
    REFERENCES TASK_LIST(ID)
    ON DELETE CASCADE
) ENGINE=INNODB;