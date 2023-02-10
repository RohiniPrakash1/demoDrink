DROP TABLE drink IF EXISTS;

CREATE TABLE drink  (
    coffee_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    brand VARCHAR(20),
    origin VARCHAR(20),
    characteristics VARCHAR(30)
);