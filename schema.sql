CREATE DATABASE IF NOT EXISTS APIDevelopSpringBoot;

USE APIDevelopSpringBoot;

CREATE TABLE author(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE book(
    id INT NOT NULL AUTO_INCREMENT,
    aisle_number INT,
    author INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(author) REFERENCES author(id),
    UNIQUE isbn
);

