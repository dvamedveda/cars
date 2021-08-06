package ru.job4j.cars.persistence;

import ru.job4j.cars.persistence.entities.Advert;

import java.util.Date;
import java.util.List;

/**
 * Класс реализующий операции с сущностями Объявления.
 */
public class AdRepository implements IRepository<Advert> {

    /**
     * Получение опубликованных объявлений, созданный в течении последних суток.
     *
     * @return список объявлений.
     */
    public List<Advert> getPublishedLastDayAdverts() {
        return this.execute(session -> session.createQuery(
                "select distinct a from Advert a "
                        + "join fetch a.user "
                        + "join fetch a.car "
                        + "left join fetch a.photos "
                        + "where a.createdOn > :minusDay "
                        + "and a.published = true "
                        + "and a.closed = false "
                        + "order by a.createdOn desc", Advert.class)
                .setParameter("minusDay", Date.from(new Date(System.currentTimeMillis()).toInstant().minusSeconds(86400)))
                .list());
    }

    /**
     * Получение опубликованных объявлений, имеющих фото.
     *
     * @return список объявлений.
     */
    public List<Advert> getPublishedAdvertsWithPhoto() {
        return this.execute(session -> session.createQuery(
                "select distinct a from Advert a "
                        + "join fetch a.user "
                        + "join fetch a.car "
                        + "left join fetch a.photos "
                        + "where a.photos.size > 0 "
                        + "and a.published = true "
                        + "and a.closed = false "
                        + "order by a.createdOn desc", Advert.class)
                .list());
    }

    /**
     * Получение всех опубликованных объявлений.
     *
     * @return
     */
    public List<Advert> getPublishedAllAdverts() {
        return this.execute(session -> session.createQuery(
                "select distinct a from Advert a "
                        + "join fetch a.user "
                        + "join fetch a.car "
                        + "left join fetch a.photos "
                        + "where a.published = true "
                        + "and a.closed = false "
                        + "order by a.createdOn desc", Advert.class)
                .list());
    }

    /**
     * Получение опубликованных объявлений, относящихся к заданной марке машины.
     *
     * @param brand марка машины.
     * @return список объявлений.
     */
    public List<Advert> getPublishedAdvertByBrandName(String brand) {
        return this.execute(session -> session.createQuery(
                "select distinct a from Advert a "
                        + "join fetch a.user "
                        + "join fetch a.car "
                        + "left join fetch a.photos "
                        + "where a.car.brand = :carBrand "
                        + "and a.published = true "
                        + "and a.closed = false "
                        + "order by a.createdOn desc", Advert.class)
                .setParameter("carBrand", brand)
                .list());
    }

    /**
     * Получение объявления по идентификатору.
     *
     * @param id идентификатор.
     * @return объект объявления.
     */
    public Advert getAdvertById(int id) {
        System.out.println("Getting advert by id " + id);
        String query = "select distinct a from Advert a "
                + "join fetch a.user "
                + "join fetch a.car "
                + "left join fetch a.photos "
                + "where a.id = :adId";
        return this.execute(session -> session.createQuery(query, Advert.class).setParameter("adId", id).getSingleResult());
    }

    /**
     * Получение объявлений конкретного пользования по id пользователя.
     *
     * @param id идентификатор пользователя.
     * @return список объявлений.
     */
    public List<Advert> getAdvertsByUserId(int id) {
        return this.execute(session ->
                session.createQuery("select distinct a from Advert a "
                        + "right join fetch a.user u "
                        + "left join fetch a.photos "
                        + "where u.id = :uId "
                        + "order by a.createdOn desc", Advert.class)
                        .setParameter("uId", id)
                        .getResultList()
        );
    }
}