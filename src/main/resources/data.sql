DELETE FROM user_role;
DELETE FROM menus;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@mail.ru', '{noop}password'),
       ('Admin', 'admin@mail.ru', '{noop}password'),
       ('AnotherUser', 'anotheruser@mail.ru', '{noop}password');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001),
       ('USER', 100002);

INSERT INTO restaurants (name)
VALUES ('Ресторан Бабель'),
       ('Ткемали'),
       ('Маленькая Италия');

INSERT INTO menus (name, price, date, restaurant_id)
VALUES ('Бефстроганов', 350.00, '2023-01-29', 100003),
       ('Сырная тарелка', 231.00, '2023-01-29', 100003),
       ('Котлета по-киевски', 205.00, '2023-01-30', 100003),
       ('Салат греческий', 310.00, '2023-01-30', 100003),
       ('Котлета по-киевски', 208.00, '2023-01-31', 100003),
       ('Салат греческий', 313.50, '2023-01-31', 100003),
       ('Хинкали', 80.00, '2023-01-29', 100004),
       ('Шашлыки', 530.00, '2023-01-29', 100004),
       ('Хачапуре по-аджарски', 200.00, '2023-01-30', 100004),
       ('Сациви с курицей', 240.00, '2023-01-30', 100004),
       ('Хачапуре по-аджарски', 205.00, '2023-01-31', 100004),
       ('Сациви с курицей', 243.50, '2023-01-31', 100004);

INSERT INTO votes (user_id, date, restaurant_id)
VALUES (100000, '2023-01-29', 100003),
       (100000, '2023-01-30', 100004),
       (100000, '2023-01-31', 100003),
       (100002, '2023-01-29', 100004),
       (100002, '2023-01-30', 100003),
       (100002, '2023-01-31', 100003);

