-- Создание таблицы photo
create table photo
(
    id        serial not null primary key,
    name      text,
    data      bytea,
    advert_id integer references advert (id)
)