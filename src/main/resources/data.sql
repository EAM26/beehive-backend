
INSERT INTO employees (id, first_name, preposition, last_name, dob, phone_number, email, password, is_employed)
VALUES
    (101, 'Jan', 'de', 'Groot', '1990-01-01', '1234567890', 'jan.degroot@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (102, 'Lotte', 'van', 'Beek', '1992-05-15', '9876543210', 'lotte.vanbeek@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (103, 'Pieter', 'van', 'Dijk', '1985-07-20', '5555555555', 'pieter.vandijk@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (104, 'Emma', 'de', 'Vries', '1998-03-10', '1111111111', 'emma.devries@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (105, 'Daan', 'van', 'Leeuwen', '1993-11-27', '9999999999', 'daan.vanleeuwen@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (106, 'Lisa', 'van', 'Dijk', '1995-09-03', '4444444444', 'lisa.vandijk@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (107, 'Thomas', 'van', 'Houten', '1991-06-08', '7777777777', 'thomas.vanhouten@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (108, 'Sophie', 'de', 'Lange', '1997-02-14', '2222222222', 'sophie.delange@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (109, 'Ruben', 'van', 'Berg', '1989-12-25', '6666666666', 'ruben.vanberg@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true),
    (110, 'Eva', 'van', 'Rijn', '1994-04-18', '8888888888', 'eva.vanrijn@example.com', '$2a$12$24jS/ugGy0ACsLCxPxj3TOqZ.95YgqBezr8yyY7UzQkD/QpuC5VMi!', true);

INSERT INTO shifts (id, start_date, end_date, start_time, end_time)
VALUES
    (201, '2023-07-01', '2023-07-01', '08:00:00', '16:00:00'),
    (202, '2023-07-02', '2023-07-02', '09:00:00', '17:00:00'),
    (203, '2023-07-03', '2023-07-03', '10:00:00', '18:00:00'),
    (204, '2023-07-04', '2023-07-04', '11:00:00', '19:00:00'),
    (205, '2023-07-05', '2023-07-05', '12:00:00', '20:00:00'),
    (206, '2023-07-06', '2023-07-06', '13:00:00', '21:00:00'),
    (207, '2023-07-07', '2023-07-07', '14:00:00', '22:00:00'),
    (208, '2023-07-08', '2023-07-08', '15:00:00', '23:00:00'),
    (209, '2023-07-09', '2023-07-09', '16:00:00', '00:00:00'),
    (210, '2023-07-10', '2023-07-10', '17:00:00', '01:00:00');


INSERT INTO teams (id, name)
VALUES
    (301, 'Kitchen'),
    (302, 'Bar'),
    (303, 'Reception'),
    (304, 'RestaurantService');


