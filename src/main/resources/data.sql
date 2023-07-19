INSERT INTO teams (team_name)
VALUES
    ('Kitchen'),
    ('Bar'),
    ('Reception'),
    ('RestaurantService');

INSERT INTO rosters (id, year, week_number, start_of_week, end_of_week) VALUES
(301, 2023, 1, '2023-01-02', '2023-01-08'),
(302, 2023, 2, '2023-01-09', '2023-01-15'),
(303, 2023, 3, '2023-01-16', '2023-01-22'),
(304, 2023, 4, '2023-01-23', '2023-01-29'),
(305, 2023, 5, '2023-01-30', '2023-02-05'),
(306, 2023, 6, '2023-02-06', '2023-02-12'),
(307, 2023, 7, '2023-02-13', '2023-02-19'),
(308, 2023, 8, '2023-02-20', '2023-02-26'),
(309, 2023, 9, '2023-02-27', '2023-03-05'),
(310, 2023, 10, '2023-03-06', '2023-03-12');

INSERT INTO employees (id, first_name, preposition, last_name, short_name, dob, phone_number, email, password, is_employed, team)
VALUES
    (101, 'Jan', 'de', 'Groot', 'Jan' ,'1990-01-01', '1234567890', 'jan.degroot@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'Kitchen'),
    (102, 'Lotte', 'van', 'Beek','Lotte' ,'1992-05-15', '9876543210', 'lotte.vanbeek@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'Bar'),
    (103, 'Pieter', 'van', 'Dijk','Pieter vD' ,'1985-07-20', '5555555555', 'pieter.vandijk@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'Kitchen'),
    (104, 'Emma', 'de', 'Vries','Emma' ,'1998-03-10', '1111111111', 'emma.devries@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'Bar'),
    (105, 'Daan', 'van', 'Leeuwen','Daan2' ,'1993-11-27', '9999999999', 'daan.vanleeuwen@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'Reception'),
    (106, 'Lisa', 'van', 'Dijk','' ,'1995-09-03', '4444444444', 'lisa.vandijk@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'Kitchen'),
    (107, 'Thomas', 'van', 'Houten','Thomas van H' ,'1991-06-08', '7777777777', 'thomas.vanhouten@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'Bar'),
    (108, 'Sophie', 'de', 'Lange','Sophie' ,'1997-02-14', '2222222222', 'sophie.delange@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'RestaurantService'),
    (109, 'Ruben', 'van', 'Berg','Ruben' ,'1989-12-25', '6666666666', 'ruben.vanberg@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'RestaurantService'),
    (110, 'Eva', 'van', 'Rijn','Eef' ,'1994-04-18', '8888888888', 'eva.vanrijn@example.com', '$2a$12$Z.Ry7lVOSASMcJ38fEX.VemDL6F6uYZWxA1PWRax0ExfNte4l1fxi', true, 'RestaurantService');

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





