INSERT INTO USERS (username, password, roles) VALUES ('admin', '$2a$12$zZpqmRzKf99zl7jGnqpkRO305x8YzzfIRWLQZrILrsEI3UN3FaPPS', 'admin,user');
INSERT INTO USERS (username, password, roles) VALUES ('user', '$2a$12$zZpqmRzKf99zl7jGnqpkRO305x8YzzfIRWLQZrILrsEI3UN3FaPPS', 'user');


INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('41', 'reueltolkien@gmail.com', 'John Ronald Reuel Tolkien', 'M', '6993995539');
INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('35', 'arthurdoyle@gmail.com', 'Arthur Conan Doyle', 'M', '6993983859');
INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('29', 'maryshelley@gmail.com', 'Mary Shelley', 'F', '6993912439');
INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('38', 'joannerowling@gmail.com', 'Joanne K. Rowling', 'F', '6993654639');


INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('44', 'georgeorwell@gmail.com', 'George Orwell', 'M', '6993622399');
INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('36', 'janeausten@gmail.com', 'Jane Austen', 'F', '6993678890');
INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('53', 'stephenking@gmail.com', 'Stephen King', 'M', '6993778890');
INSERT INTO Author (age, email, full_name, gender, phone) VALUES ('47', 'marktwain@gmail.com', 'Mark Twain', 'M', '6993218870');


INSERT INTO Book (title, isbn, price, author_id) VALUES ('The Lord of the Rings: 50th Anniversary, One Vol. Edition', '12341436', 30.99, 1);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('Sherlock Holmes', '12342436', 45.99, 2);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('Frankenstein', '12313436', 39.99, 3);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('Harry Potter, 1: Harry Potter and the Philosophers Stone', '12341436', 41.99, 4);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('Hobbit', '15391436', 1.99, 1);


INSERT INTO Book (title, isbn, price, author_id) VALUES ('1984', '22334455', 29.99, 5);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('Pride and Prejudice', '33445566', 19.99, 6);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('The Shining', '44556677', 24.99, 7);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('Adventures of Huckleberry Finn', '55667788', 21.99, 8);
INSERT INTO Book (title, isbn, price, author_id) VALUES ('Animal Farm', '66778899', 14.99, 5);


INSERT INTO Stock (id , quantity) VALUES (1, 5);
INSERT INTO Stock (id , quantity) VALUES (2, 2);
INSERT INTO Stock (id , quantity) VALUES (3, 10);
INSERT INTO Stock (id , quantity) VALUES (4, 8);


INSERT INTO Stock (id , quantity) VALUES (6, 7);
INSERT INTO Stock (id , quantity) VALUES (7, 4);
INSERT INTO Stock (id , quantity) VALUES (8, 9);
INSERT INTO Stock (id , quantity) VALUES (9, 12);


--ALTER TABLE Book ALTER COLUMN id RESTART WITH 6;
