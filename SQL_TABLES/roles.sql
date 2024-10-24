CREATE TABLE roles (
                       id                    serial,
                       name                  varchar(50) NOT NULL,
                       PRIMARY KEY (id)
);
INSERT INTO roles (name)
VALUES
    ('ROLE_USER'), ('ROLE_ADMIN');
