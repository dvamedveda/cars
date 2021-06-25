-- Создание таблицы advert
create table advert
(
    id          serial not null primary key,
    summary     text,
    description text,
    closed      boolean,
    users_id    integer references users (id),
    car_id      integer unique references car (id),
    createdOn  timestamp,
    closedOn   timestamp,
    price       integer
)