-- Создание таблицы users
create table users
(
    id      serial not null primary key,
    name    text unique,
    created timestamp
)