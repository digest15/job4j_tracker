create table if not exists users (
    id serial primary key,
    login text not null unique ,
    password text
);

INSERT INTO users (login, password) VALUES ('Ivanov', 'root');
INSERT INTO users (login, password) VALUES ('Petrov', 'root');
INSERT INTO users (login, password) VALUES ('Sidorov', 'root');