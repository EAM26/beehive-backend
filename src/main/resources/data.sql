
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
--
--
-- INSERT INTO employees (id, first_name, preposition, last_name, dob, phone_number, email, password, is_employed)
-- VALUES
--     (101, 'John', '', 'Doe', '1990-01-01', '1234567890', 'john.doe@example.com', 'Password123!', true),
--     (102, 'Jane', '', 'Smith', '1992-05-15', '9876543210', 'jane.smith@example.com', 'Password456!', true),
--     (103, 'Michael', '', 'Johnson', '1985-07-20', '5555555555', 'michael.johnson@example.com', 'Password789!', true),
--     (104, 'Emily', '', 'Brown', '1998-03-10', '1111111111', 'emily.brown@example.com', 'Password123!', true),
--     (105, 'David', '', 'Taylor', '1993-11-27', '9999999999', 'david.taylor@example.com', 'Password456!', true),
--     (106, 'Emma', '', 'Anderson', '1995-09-03', '4444444444', 'emma.anderson@example.com', 'Password789!', true),
--     (107, 'Christopher', '', 'Wilson', '1991-06-08', '7777777777', 'christopher.wilson@example.com', 'Password123!', true),
--     (108, 'Olivia', '', 'Martinez', '1997-02-14', '2222222222', 'olivia.martinez@example.com', 'Password456!', true),
--     (109, 'Daniel', '', 'Thomas', '1989-12-25', '6666666666', 'daniel.thomas@example.com', 'Password789!', true),
--     (110, 'Sophia', '', 'Lee', '1994-04-18', '8888888888', 'sophia.lee@example.com', 'Password123!', true);


INSERT INTO shifts (id, start_time, end_time)
VALUES (201, '2023-06-12 08:00:00', '2023-06-12 12:00:00'),
       (202, '2023-06-12 13:00:00', '2023-06-12 16:00:00'),
       (203, '2023-06-12 17:30:00', '2023-06-12 20:30:00'),
       (204, '2023-06-12 21:00:00', '2023-06-12 23:00:00'),
       (205, '2023-06-12 23:30:00', '2023-06-13 02:30:00'),
       (206, '2023-06-13 07:00:00', '2023-06-13 10:00:00'),
       (207, '2023-06-13 11:30:00', '2023-06-13 14:30:00'),
       (208, '2023-06-13 15:00:00', '2023-06-13 18:00:00'),
       (209, '2023-06-13 19:30:00', '2023-06-13 22:30:00'),
       (210, '2023-06-13 23:00:00', '2023-06-14 02:00:00'),
       (211, '2023-06-14 06:00:00', '2023-06-14 09:00:00'),
       (212, '2023-06-14 10:30:00', '2023-06-14 13:30:00'),
       (213, '2023-06-14 14:00:00', '2023-06-14 17:00:00'),
       (214, '2023-06-14 18:30:00', '2023-06-14 21:30:00'),
       (215, '2023-06-14 22:00:00', '2023-06-15 01:00:00'),
       (216, '2023-06-15 05:00:00', '2023-06-15 08:00:00'),
       (217, '2023-06-15 09:30:00', '2023-06-15 12:30:00'),
       (218, '2023-06-15 13:00:00', '2023-06-15 16:00:00'),
       (219, '2023-06-15 17:30:00', '2023-06-15 20:30:00'),
       (220, '2023-06-15 21:00:00', '2023-06-15 23:00:00');

