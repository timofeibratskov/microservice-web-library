CREATE TABLE users (
                       id                    bigserial,
                       username              varchar(30) NOT NULL UNIQUE,
                       password              varchar(80) NOT NULL,
                       email                 varchar(50) UNIQUE,
                       PRIMARY KEY (id)
);

INSERT INTO users (username, password, email)
VALUES
    ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com'),
    ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'admin@gmail.com');
--!!!!!!!!!!!!!!пароли это число 100