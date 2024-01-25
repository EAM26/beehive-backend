INSERT INTO teams (team_name, is_active)
VALUES ('Kitchen', true),
       ('Bar', true),
       ('Reception', true),
       ('RestaurantService', true),
       ('OldTeam', false);

INSERT INTO rosters(id, week, year, team_name)
VALUES (1, 52, 2022, 'Kitchen'),
       (2, 52, 2022, 'Bar'),
       (3, 52, 2022, 'Reception'),
       (4, 52, 2022, 'RestaurantService'),
       (5, 1, 2023, 'Kitchen'),
       (6, 1, 2023, 'Bar'),
       (7, 1, 2023, 'Reception'),
       (8, 1, 2023, 'RestaurantService');


INSERT INTO users (username, password, email, is_deleted)
VALUES ('drone', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'drone@test.nl', false),
       ('bee', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'bee@test.nl', false),
       ('queen', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'queen@test.nl', false),
       ('user104', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user104@test.nl', false),
       ('user105', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user105@test.nl', false),
       ('user106', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user106@test.nl', false),
       ('user107', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user107@test.nl', false),
       ('user108', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user108@test.nl', false),
       ('user109', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user109@test.nl', false),
       ('user110', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user110@test.nl', false),
       ('user111', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user111@test.nl', false),
       ('user112', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user112@test.nl', false),
       ('user113', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user113@test.nl', false),
       ('user114', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user114@test.nl', false),
       ('user115', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user115@test.nl', false),
       ('user116', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user116@test.nl', false),
       ('user117', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user117@test.nl', false),
       ('user118', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user118@test.nl', false),
       ('user119', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user119@test.nl', false),
       ('user120', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user120@test.nl', false),
       ('user121', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'user121@test.nl', true),
       ('userNoEmp', '$2a$12$3seFwZfJ/gm3E2VFHqz7g.T465Jw/IDe8.sibEOE9RsmCLzr6KUke', 'userNoEmp@test.nl', false);


INSERT INTO employees (id, first_name, preposition, last_name, short_name, dob, phone_number,
                       is_active, team_name, username)
VALUES (101, 'Jan', 'de', 'Groot', 'Jan', '1990-01-01', '1234567890', true, 'Kitchen', 'drone'),
       (102, 'Lotte', 'van', 'Beek', 'Lotte', '1992-05-15', '9876543210', true, 'Kitchen', 'bee'),
       (103, 'Pieter', 'van', 'Dijk', 'Pieter vD', '1985-07-20', '5555555555', true, 'Kitchen', 'queen'),
       (104, 'Emma', 'de', 'Vries', 'Emma', '1998-03-10', '1111111111', true, 'Bar', 'user104'),
       (105, 'Daan', 'van', 'Leeuwen', 'Daan2', '1993-11-27', '9999999999', false, 'Bar', 'user105'),
       (106, 'Lisa', 'van', 'Dijk', 'Lies', '1995-09-03', '4444444444', true, 'Bar', 'user106'),
       (107, 'Thomas', 'van', 'Houten', 'Thomas van H', '1991-06-08', '7777777777', true, 'Bar', 'user107'),
       (108, 'Sophie', 'de', 'Lange', 'Sophie', '1997-02-14', '2222222222', false, 'Reception', 'user108'),
       (109, 'Ruben', 'van', 'Berg', 'Ruben', '1989-12-25', '6666666666', true, 'Reception', 'user109'),
       (110, 'Eva', 'van', 'Rijn', 'Eef', '1994-04-18', '8888888888', true, 'RestaurantService', 'user110'),
       (111, 'Eva', 'de', 'Vries', 'evadv', '1993-07-12', '123-456-7890', true, 'Kitchen', 'user111'),
       (112, 'Daan', 'van der', 'Linden', 'daanvl', '1991-04-28', '987-654-3210', true, 'Bar', 'user112'),
       (113, 'Emma', '', 'Bakker', 'emmab', '1995-09-14', '555-123-4567', true, 'Reception', 'user113'),
       (114, 'Liam', '', 'Jansen', 'liamj', '1990-12-03', '999-888-7777', true, 'RestaurantService', 'user114'),
       (115, 'Sophie', '', 'Hendriks', 'sophieh', '1988-02-19', '111-222-3333', true, 'Kitchen', 'user115'),
       (116, 'Sem', '', 'Bos', 'semb', '1992-06-30', '444-555-6666', true, 'Bar', 'user116'),
       (117, 'Tess', '', 'Willems', 'tessw', '1994-03-25', '777-888-9999', true, 'Reception', 'user117'),
       (118, 'Milan', '', 'Maas', 'milanm', '1997-11-10', '222-333-4444', true, 'RestaurantService', 'user118'),
       (119, 'Julia', '', 'Peeters', 'juliap', '1989-08-07', '888-999-0000', true, 'Kitchen', 'user119'),
       (120, 'Lucas', '', 'Dekker', 'lucasd', '1996-10-21', '333-444-5555', true, 'Bar', 'user120');

INSERT INTO absences(id, start_date, end_date, employee_id)
VALUES (301, '2023-01-15', '2023-01-18', 101),
       (302, '2023-01-20', '2023-01-25', 101),
       (303, '2023-01-30', '2023-01-30', 101),
       (304, '2023-02-22', '2023-02-26', 101),
       (305, '2023-01-19', '2023-01-22', 103),
       (306, '2023-01-09', '2023-01-10', 103);

INSERT INTO authorities (username, authority)
VALUES ('drone', 'ROLE_USER'),
       ('bee', 'ROLE_MANAGER'),
       ('queen', 'ROLE_ADMIN'),
       ('user104', 'ROLE_USER'),
       ('user105', 'ROLE_USER'),
       ('user106', 'ROLE_USER'),
       ('user107', 'ROLE_USER'),
       ('user108', 'ROLE_USER'),
       ('user109', 'ROLE_USER'),
       ('user110', 'ROLE_USER'),
       ('user111', 'ROLE_USER'),
       ('user112', 'ROLE_USER'),
       ('user113', 'ROLE_USER'),
       ('user114', 'ROLE_USER'),
       ('user115', 'ROLE_USER'),
       ('user116', 'ROLE_USER'),
       ('user117', 'ROLE_USER'),
       ('user118', 'ROLE_USER'),
       ('user119', 'ROLE_USER'),
       ('user120', 'ROLE_USER'),
       ('user121', 'ROLE_USER'),
       ('userNoEmp', 'ROLE_USER');


INSERT INTO shifts (id, start_shift, end_shift, team_name, employee_id, roster_id, week_number, year)
VALUES (201, '2023-01-04 09:00:00', '2023-01-04 17:00:00', 'Kitchen', 101, 5, 1, 2023),
       (202, '2023-01-02 10:00:00', '2023-01-02 17:00:00', 'Kitchen', 102, 5, 1, 2023),
       (203, '2023-01-02 12:00:00', '2023-01-02 17:00:00', 'Kitchen', 103, 5, 1, 2023),
       (204, '2023-01-02 17:00:00', '2023-01-03 02:00:00', 'Kitchen', 111, 5, 1, 2023),
       (205, '2023-01-02 22:00:00', '2023-01-03 02:00:00', 'Kitchen', 115, 5, 1, 2023),
       (206, '2023-01-03 09:00:00', '2023-01-03 17:00:00', 'Kitchen', 101, 5, 1, 2023),
       (207, '2023-01-03 10:00:00', '2023-01-03 17:00:00', 'Kitchen', 102, 5, 1, 2023),
       (208, '2023-01-03 12:00:00', '2023-01-03 17:00:00', 'Kitchen', 103, 5, 1, 2023),
       (209, '2023-01-03 17:00:00', '2023-01-03 21:00:00', 'Kitchen', 111, 5, 1, 2023),
       (210, '2023-01-03 17:00:00', '2023-01-04 02:00:00', 'Kitchen', 115, 5, 1, 2023),
       (211, '2023-01-03 17:00:00', '2023-01-04 02:00:00', 'Kitchen', 119, 5, 1, 2023),
       (212, '2023-01-03 17:00:00', '2023-01-04 02:00:00', 'Bar', null, 6, 1, 2023),
       (213, '2023-01-03 17:00:00', '2023-01-04 02:00:00', 'Bar', null, 6, 1, 2023),
       (214, '2023-01-02 09:00:00', '2023-01-02 17:00:00', 'Kitchen', 101, 5, 1, 2023),
       (215, '2023-01-04 10:00:00', '2023-01-04 17:00:00', 'Kitchen', 102, 5, 1, 2023),
       (216, '2023-01-04 17:00:00', '2023-01-04 23:00:00', 'Kitchen', 103, 5, 1, 2023),
       (217, '2023-01-04 17:00:00', '2023-01-04 23:00:00', 'Reception', null, 7, 1, 2023),
       (218, '2023-01-04 17:00:00', '2023-01-05 02:00:00', 'RestaurantService', null, 8, 1, 2023);






