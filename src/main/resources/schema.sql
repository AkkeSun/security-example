create table MEMBER
(
    TABLE_INDEX  int auto_increment primary key,
    USERNAME varchar(50)  not null,
    PASSWORD varchar(100) not null,
    SECRET_KEY varchar(100) not null,
    ROLE varchar(50) not null
);