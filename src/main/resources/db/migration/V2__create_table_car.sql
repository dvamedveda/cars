-- Создание таблицы car
create table car
(
    id       serial not null primary key,
    brand    text,
    model    text,
    body     text,
    produced timestamp
)