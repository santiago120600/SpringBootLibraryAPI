CREATE DATABASE IF NOT EXISTS APIDevelopSpringBoot;

USE APIDevelopSpringBoot;

CREATE TABLE author(
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE book(
    id INT NOT NULL AUTO_INCREMENT,
    aisle_number INT,
    author_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(25) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(author_id) REFERENCES author(id),
    UNIQUE isbn
);

