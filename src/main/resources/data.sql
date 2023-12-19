INSERT INTO teams (id, team_name)
VALUES (401, 'Kitchen'),
       (402, 'Bar'),
       (403, 'Reception'),
       (404, 'RestaurantService');

INSERT INTO rosters (id, year, week_number, start_of_week, end_of_week, team_id)
VALUES ('1-2023-Kitchen', 2023, 1, '2023-01-02', '2023-01-08', 401),
       ('2-2023-Kitchen', 2023, 2, '2023-01-09', '2023-01-15', 401),
       ('3-2023-Kitchen', 2023, 3, '2023-01-16', '2023-01-22', 401),
       ('4-2023-Kitchen', 2023, 4, '2023-01-23', '2023-01-29', 401),
       ('1-2023-Bar', 2023, 1, '2023-01-02', '2023-01-08', 402),
       ('2-2023-Bar', 2023, 2, '2023-01-09', '2023-01-15', 402),
       ('3-2023-Bar', 2023, 3, '2023-01-16', '2023-01-22', 402),
       ('4-2023-Bar', 2023, 4, '2023-01-23', '2023-01-29', 402),
       ('1-2023-Reception', 2023, 1, '2023-01-02', '2023-01-08', 403),
       ('1-2023-RestaurantService', 2023, 1, '2023-01-02', '2023-01-08', 404);

INSERT INTO employees (id, first_name, preposition, last_name, short_name, dob, phone_number,
                       is_employed, team_id)
VALUES (101, 'Jan', 'de', 'Groot', 'Jan', '1990-01-01', '1234567890', true, 401),
       (102, 'Lotte', 'van', 'Beek', 'Lotte', '1992-05-15', '9876543210', true, 401),
       (103, 'Pieter', 'van', 'Dijk', 'Pieter vD', '1985-07-20', '5555555555', false, 401),
       (104, 'Emma', 'de', 'Vries', 'Emma', '1998-03-10', '1111111111', true, 402),
       (105, 'Daan', 'van', 'Leeuwen', 'Daan2', '1993-11-27', '9999999999', false, 402),
       (106, 'Lisa', 'van', 'Dijk', 'Lies', '1995-09-03', '4444444444', true, 402),
       (107, 'Thomas', 'van', 'Houten', 'Thomas van H', '1991-06-08', '7777777777', true, 402),
       (108, 'Sophie', 'de', 'Lange', 'Sophie', '1997-02-14', '2222222222', false, 403),
       (109, 'Ruben', 'van', 'Berg', 'Ruben', '1989-12-25', '6666666666', true, 403),
       (110, 'Eva', 'van', 'Rijn', 'Eef', '1994-04-18', '8888888888', true, 404),
       (111, 'Eva', 'de', 'Vries', 'evadv', '1993-07-12', '123-456-7890', true, 401),
       (112, 'Daan', 'van der', 'Linden', 'daanvl', '1991-04-28', '987-654-3210', true, 402),
       (113, 'Emma', '', 'Bakker', 'emmab', '1995-09-14', '555-123-4567',true, 403),
       (114, 'Liam', '', 'Jansen', 'liamj', '1990-12-03', '999-888-7777', true, 404),
       (115, 'Sophie', '', 'Hendriks', 'sophieh', '1988-02-19', '111-222-3333', true, 401),
       (116, 'Sem', '', 'Bos', 'semb', '1992-06-30', '444-555-6666', true, 402),
       (117, 'Tess', '', 'Willems', 'tessw', '1994-03-25', '777-888-9999', true, 403),
       (118, 'Milan', '', 'Maas', 'milanm', '1997-11-10', '222-333-4444', true, 404),
       (119, 'Julia', '', 'Peeters', 'juliap', '1989-08-07', '888-999-0000', true, 401),
       (120, 'Lucas', '', 'Dekker', 'lucasd', '1996-10-21', '333-444-5555', true, 402);


INSERT INTO shifts (id, start_date, end_date, start_time, end_time, employee_id, roster_id)
VALUES (201, '2023-01-02', '2023-01-02', '09:00:00', '17:00:00', 101, '1-2023-Kitchen'),
       (202, '2023-01-02', '2023-01-02', '10:00:00', '17:00:00', 102, '1-2023-Kitchen'),
       (203, '2023-01-02', '2023-01-02', '12:00:00', '17:00:00', 103, '1-2023-Kitchen'),
       (204, '2023-01-02', '2023-01-03', '17:00:00', '02:00:00', 111, '1-2023-Kitchen'),
       (205, '2023-01-02', '2023-01-03', '22:00:00', '02:00:00', 115, '1-2023-Kitchen'),
       (206, '2023-01-03', '2023-01-03', '09:00:00', '17:00:00', 101, '1-2023-Kitchen'),
       (207, '2023-01-03', '2023-01-03', '10:00:00', '17:00:00', 102, '1-2023-Kitchen'),
       (208, '2023-01-03', '2023-01-03', '12:00:00', '17:00:00', 103, '1-2023-Kitchen'),
       (209, '2023-01-03', '2023-01-03', '17:00:00', '21:00:00', 111, '1-2023-Kitchen'),
       (210, '2023-01-03', '2023-01-04', '17:00:00', '02:00:00', 115, '1-2023-Kitchen'),
       (211, '2023-01-03', '2023-01-04', '17:00:00', '02:00:00', 119, '1-2023-Kitchen'),
       (212, '2023-01-03', '2023-01-04', '17:00:00', '02:00:00', null, '1-2023-Kitchen'),
       (213, '2023-01-03', '2023-01-04', '17:00:00', '02:00:00', null, '1-2023-Kitchen'),
       (214, '2023-01-04', '2023-01-04', '09:00:00', '17:00:00', 101, '1-2023-Kitchen'),
       (215, '2023-01-04', '2023-01-04', '10:00:00', '17:00:00', 102, '1-2023-Kitchen'),
       (216, '2023-01-04', '2023-01-04', '17:00:00', '23:00:00', 103, '1-2023-Kitchen'),
       (217, '2023-01-04', '2023-01-04', '17:00:00', '23:00:00', null, '1-2023-Kitchen'),
       (218, '2023-01-04', '2023-01-05', '17:00:00', '02:00:00', null, '1-2023-Kitchen');

-- Wachtwoorden zijn username van user
INSERT INTO users (username, password, email, employee_id)
VALUES ('drone', '$2a$12$6VTVyOxi3R7rvm.3QgVVtOB33FePKmA4ZHWyvu3C8AM9Xuvo.nKdK', 'drone@test.nl', 101),
       ('bee', '$2a$12$gVMFeZOh9t5IqDnyrUSls.2.at7yQip0HivRy1JDTKpIRqakvQq6e', 'bee@test.nl', 102),
       ('queen', '$2a$12$m2474RWG2d6NfKWIfBWOTun4DySP9ilW8sDqoSOL6ub3EpEYkA1XC', 'queen@test.nl', 103),
       ('user104', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user104@test.nl', 104),
       ('user105', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user105@test.nl', 105),
       ('user106', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user106@test.nl', 106),
       ('user107', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user107@test.nl', 107),
       ('user108', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user108@test.nl', 108),
       ('user109', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user109@test.nl', 109),
       ('user110', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user110@test.nl', 110),
       ('user111', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user111@test.nl', 111),
       ('user112', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user112@test.nl', 112),
       ('user113', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user113@test.nl', 113),
       ('user114', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user114@test.nl', 114),
       ('user115', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user115@test.nl', 115),
       ('user116', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user116@test.nl', 116),
       ('user117', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user117@test.nl', 117),
       ('user118', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user118@test.nl', 118),
       ('user119', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user119@test.nl', 119),
       ('user120', '$2a$12$8kZ5gsRWK1d1W6NcP6FMPukVQFK.38.fCeSmfoVY3wR7r5D1VBdae', 'user120@test.nl', 120);



INSERT INTO authorities (username, authority)
VALUES ('droneOne', 'ROLE_USER'),
       ('beeOne', 'ROLE_USER'),
       ('beeOne', 'ROLE_MANAGER'),
       ('queen', 'ROLE_USER'),
       ('queen', 'ROLE_MANAGER'),
       ('queen', 'ROLE_ADMIN');







