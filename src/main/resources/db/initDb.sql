DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS person_role;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS menu_item;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS dish;

DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE person
(
    id         INT       DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    name       VARCHAR(50)             NOT NULL,
    email      VARCHAR(50)             NOT NULL,
    password   VARCHAR(50)             NOT NULL,
    registered TIMESTAMP DEFAULT NOW() NOT NULL,
    enabled    BOOLEAN   DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX person_unique_email_idx ON person (email);

CREATE TABLE role
(
    id        INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL
);
CREATE UNIQUE INDEX role_unique_name_idx ON role (role_name);

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
    id      INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    name    VARCHAR(50)  NOT NULL,
    address VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX restaurant_unique_name_address_idx ON restaurant (name, address);

CREATE TABLE dish
(
    id          INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    description VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX dish_unique_descr_idx ON dish (description);

CREATE TABLE menu
(
    id      INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    rest_id INT  NOT NULL,
    date    DATE NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurant (id)
);
CREATE UNIQUE INDEX menu_unique_rest_id_date_idx ON menu (rest_id, date);

CREATE TABLE menu_item
(
    id      INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    menu_id INT NOT NULL,
    dish_id INT NOT NULL,
    price   DECIMAL(6, 2),
    FOREIGN KEY (menu_id) REFERENCES menu (id),
    FOREIGN KEY (dish_id) REFERENCES dish (id)
);
CREATE UNIQUE INDEX menu_item_unique_menu_id_dish_id_idx ON menu_item (menu_id, dish_id);

CREATE TABLE vote
(
    id        INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    person_id INT  NOT NULL,
    menu_id   INT  NOT NULL,
    vote_date DATE NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person (id),
    FOREIGN KEY (menu_id) REFERENCES menu (id),
    CONSTRAINT chk_vote_date CHECK (vote_date = (SELECT date
                                                 FROM menu
                                                 WHERE id = menu_id))
);
CREATE UNIQUE INDEX vote_unique_vote_person_id_menu_id_idx ON vote (person_id, vote_date);
