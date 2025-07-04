CREATE TABLE USERS (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255) NOT NULL
);

INSERT INTO USERS (username, password, roles) VALUES ('admin', 'password', 'admin,user');