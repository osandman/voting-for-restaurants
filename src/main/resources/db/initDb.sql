DROP TABLE IF EXISTS person_role;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS role;

DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE person
(
    id       INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    email    VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE role
(
    id        INT DEFAULT NEXTVAL('global_seq') PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE person_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES person (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);




