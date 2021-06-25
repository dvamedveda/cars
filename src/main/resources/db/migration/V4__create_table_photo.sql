-- Создание таблицы photo
create table photo
(
    id        serial not null primary key,
    name      text,
    data      text,
    advert_id integer references advert (id)
)