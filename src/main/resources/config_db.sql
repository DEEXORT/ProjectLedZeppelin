DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id bigserial,
    login varchar(100),
    password varchar(100),
    playerId bigint
);

INSERT INTO users (login, password, playerId)
VALUES ('admin','admin', null);
