DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS person_role;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS menu_item;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS dish;

DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq AS INTEGER START WITH 100000;

CREATE TABLE person
(
    id         INT       DEFAULT nextval('global_seq') PRIMARY KEY,
    name       VARCHAR(100)            NOT NULL,
    email      VARCHAR(100)            NOT NULL,
    password   VARCHAR(100)            NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL,
    enabled    BOOLEAN   DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX person_unique_email_idx ON person (email);

CREATE TABLE role
(
    id   INT DEFAULT nextval('global_seq') PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    CONSTRAINT role_name_idx UNIQUE (type)
);

CREATE TABLE person_role
(
    person_id INT NOT NULL,
    role_id   INT NOT NULL,
    PRIMARY KEY (person_id, role_id),
    FOREIGN KEY (person_id) REFERENCES person (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE restaurant
(
    id      INT DEFAULT nextval('global_seq') PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    CONSTRAINT name_address UNIQUE (name, address)
);

CREATE TABLE dish
(
    id          INT DEFAULT nextval('global_seq') PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL
);
CREATE UNIQUE INDEX dish_unique_name_idx ON dish (name);

CREATE TABLE menu
(
    id      INT DEFAULT nextval('global_seq') PRIMARY KEY,
    rest_id INT  NOT NULL,
    date    DATE NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT rest_id_date_idx UNIQUE (rest_id, date)
);
CREATE INDEX menu_date_idx ON menu (date);

CREATE TABLE menu_item
(
    id      INT DEFAULT nextval('global_seq') PRIMARY KEY,
    menu_id INT NOT NULL,
    dish_id INT NOT NULL,
    amount   DECIMAL(6, 2) NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE,
    CONSTRAINT menu_id_dish_id_idx UNIQUE (menu_id, dish_id)
);

CREATE TABLE vote
(
    id        INT DEFAULT nextval('global_seq') PRIMARY KEY,
    person_id INT  NOT NULL,
    menu_id   INT  NOT NULL,
    vote_date DATE NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    CHECK (vote_date = (SELECT date
                        FROM menu
                        WHERE id = menu_id)),
    CONSTRAINT person_id_vote_date_idx UNIQUE (person_id, vote_date)
);
CREATE INDEX vote_date_idx ON vote (vote_date);
