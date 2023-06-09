DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS menus;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

CREATE TABLE users
(
    id               INTEGER GENERATED BY DEFAULT AS IDENTITY SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name             VARCHAR(255)            NOT NULL,
    email            VARCHAR(255)            NOT NULL,
    password         VARCHAR(255)            NOT NULL,
    registered       TIMESTAMP DEFAULT now() NOT NULL,
    enabled          BOOLEAN   DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL UNIQUE,
    role    VARCHAR(255) NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    CONSTRAINT restaurants_unique_name_idx UNIQUE (name)
);

CREATE TABLE menus
(
    id             INTEGER GENERATED BY DEFAULT AS IDENTITY SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name           VARCHAR(255)                NOT NULL,
    price          DECIMAL(16,2)               NOT NULL,
    date           DATE DEFAULT CURRENT_DATE   NOT NULL,
    restaurant_id  INTEGER                     NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX menus_unique_restaurant_date_name_idx ON menus (restaurant_id, date, name);

CREATE TABLE votes
(
    id             INTEGER GENERATED BY DEFAULT AS IDENTITY SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    user_id        INTEGER NOT NULL,
    date           DATE DEFAULT CURRENT_DATE   NOT NULL,
    restaurant_id  INTEGER NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX votes_unique_user_date_idx ON votes (user_id, date);