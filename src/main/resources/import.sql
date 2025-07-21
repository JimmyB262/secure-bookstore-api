INSERT INTO USERS (username, password, roles) VALUES ('admin', '$2a$12$zZpqmRzKf99zl7jGnqpkRO305x8YzzfIRWLQZrILrsEI3UN3FaPPS', 'admin,user');
INSERT INTO USERS (username, password, roles) VALUES ('user', '$2a$12$zZpqmRzKf99zl7jGnqpkRO305x8YzzfIRWLQZrILrsEI3UN3FaPPS', 'user');

INSERT INTO Author (age, author_id, email, full_name, gender, phone) VALUES ('41', '1', 'kostas123@gmail.com', 'Kostas Papadopoulos', 'M', '6993995539');
INSERT INTO Author (age, author_id, email, full_name, gender, phone) VALUES ('35', '2', 'petros123@gmail.com', 'Petros Papadopoulos', 'M', '6993983859');
INSERT INTO Author (age, author_id, email, full_name, gender, phone) VALUES ('29', '3', 'argyrhs123@gmail.com', 'Argyrhs Papadopoulos', 'M', '6993912439');
INSERT INTO Author (age, author_id, email, full_name, gender, phone) VALUES ('38', '4', 'sofia459@gmail.com', 'Sofia Papadopoulou', 'F', '6993654639');

INSERT INTO Book (id, title, isbn, price, author_id) VALUES (1, 'Syntages me ton Akh', '12341436', 30.99, 1);
INSERT INTO Book (id, title, isbn, price, author_id) VALUES (2, 'Dokimio', '12342436', 45.99, 2);
INSERT INTO Book (id, title, isbn, price, author_id) VALUES (3, 'Poihmata', '12313436', 39.99, 3);
INSERT INTO Book (id, title, isbn, price, author_id) VALUES (4, 'Anthologio', '12341436', 41.99, 4);

INSERT INTO Stock (id , quantity) VALUES (1, 5);
INSERT INTO Stock (id , quantity) VALUES (2, 2);
INSERT INTO Stock (id , quantity) VALUES (3, 10);
INSERT INTO Stock (id , quantity) VALUES (4, 8);