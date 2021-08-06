-- Создание таблицы users
create table users
(
    id       serial not null primary key,
    name     text,
    email    text unique,
    password text,
    created  timestamp
)