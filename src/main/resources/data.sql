INSERT INTO person (name, email, registered, password)
VALUES ('Игорь Воробьев', 'admin@ya.ru', '2023-01-01 12:01:01', '{noop}igor123'),         -- 100000
       ('Виктория Санникова', 'vika@google.com', '2023-05-25 11:59:59', '{noop}vika123'), -- 100001
       ('Михаил Гусев', 'misha@yahoo.com', now(), '{noop}misha123'); -- 100002

INSERT INTO role (type)
VALUES ('ADMIN'), -- 100003
       ('REGULAR'); -- 100004

INSERT INTO person_role (person_id, role_id)
VALUES (100000, 100003),
       (100000, 100004),
       (100001, 100004),
       (100002, 100004);

INSERT INTO restaurant(name, address)
VALUES ('Хмельная миля', 'Северное ш., д.50'),                    -- 100005
       ('Дед Мазай и дичь на гриле', 'Ивана Сусанина ул., д.1А'), -- 100006
       ('Карнавал', 'Шумана ул., д.9'); -- 100007

INSERT INTO menu(rest_id, date)
VALUES (100005, now()),        -- 100008
       (100005, '2023-08-02'), -- 100009
       (100006, '2023-08-01'), -- 100010
       (100007, now()); -- 100011

INSERT INTO dish(name, description)
VALUES ('Беллини',
        'Алкогольный коктейль, изобретённый в Венеции в первой половине XX века. Представляет собой смесь игристого вина и персикового пюре'),                                       -- 100012
       ('Ахи-поке',
        'Традиционное гавайское блюдо, представляет собой закуску-салат, ингредиенты в котором не перемешиваются а раскладываются поверх крупы (рис, киноа)'),                       -- 100013
       ('Крудо', 'Это блюдо из тонких ломтиков свежайшей рыбы, немного подмаринованных в лимонном соке'),                                                                            -- 100014
       ('Окономияки',
        'Японское блюдо из разряда фастфуда, жареная лепёшка из смеси разнообразных ингредиентов, смазанная специальным соусом и посыпанная очень тонко нарезанным сушёным тунцом'), -- 100015
       ('Тальята',
        'Классическое итальянское блюдо из тонко нарезанных кусочков мяса (чаще говядины) со специями'),                                                                             -- 100016
       ('Средиземноморский сибас', 'Морская рыба семейства окунеобразных'),                                                                                                          -- 100017
       ('Стейк из кенгуру', ''),                                                                                                                                                     -- 100018
       ('Теплый салат «Бурре»', 'aaa'); -- 100019

INSERT INTO menu_item(menu_id, dish_id, amount)
VALUES (100008, 100019, 230.50),
       (100008, 100018, 120.99),
       (100008, 100017, 404.04),
       (100009, 100012, 15.2),
       (100009, 100013, 147.74),
       (100010, 100012, 1850.01),
       (100010, 100015, 3.33),
       (100011, 100016, 99.55);

INSERT INTO vote(person_id, menu_id, vote_date)
VALUES (100000, 100008, now()),
       (100000, 100009, '2023-08-02'),
       (100001, 100010, '2023-08-01'),
       (100001, 100011, now());

