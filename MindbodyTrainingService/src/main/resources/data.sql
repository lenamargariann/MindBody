CREATE TABLE IF NOT EXISTS trainer_workload (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    training_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    training_duration INTEGER NOT NULL CHECK (training_duration BETWEEN 30 AND 300)
    );

INSERT INTO trainer_workload (username, firstname, lastname, is_active, training_date, training_duration)
VALUES ('john.doe01', 'John', 'Doe', TRUE, '2023-01-10 10:00', 60),
       ('jane.doe02', 'Jane', 'Doe', FALSE, '2023-02-11 11:00', 90),
       ('john.doe01', 'John', 'Doe', TRUE, '2022-03-12 09:00', 120),
       ('bob.jones04', 'Bob', 'Jones', TRUE, '2023-04-13 08:30', 45),
       ('dave.brown06', 'Dave', 'Brown', TRUE, '2022-10-14 14:00', 75),
       ('dave.brown06', 'Dave', 'Brown', TRUE, '2023-06-15 16:00', 90),
       ('eve.green07', 'Eve', 'Green', FALSE, '2023-07-16 17:00', 60),
       ('dave.brown06', 'Dave', 'Brown', TRUE, '2023-08-17 18:00', 80),
       ('grace.black09', 'Grace', 'Black', FALSE, '2023-09-18 19:00', 50),
       ('dave.brown06', 'Dave', 'Brown', TRUE, '2022-10-19 20:00', 100);
