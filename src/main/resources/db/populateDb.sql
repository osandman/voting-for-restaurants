INSERT INTO person (name, email, password)
VALUES ('admin@ya.ru', 'Игорь Воробьев', 'igor123'),
       ('vika@google.com', 'Виктория Санникова', 'vika123'),
       ('misha@yahoo.com', 'Михаил Гусев', 'misha123');

INSERT INTO role (role_name)
VALUES ('admin'),
       ('regular');

INSERT INTO person_role (user_id, role_id)
VALUES (100000, 100003),
       (100000, 100004);