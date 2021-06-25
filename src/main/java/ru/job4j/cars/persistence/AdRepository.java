package ru.job4j.cars.persistence;

import ru.job4j.cars.persistence.entities.Advert;

import java.util.Date;
import java.util.List;

/**
 * Класс реализующий операции с сущностями Объявления.
 */
public class AdRepository implements IRepository<Advert> {

    /**
     * Получение объявлений, созданный в течении последних суток.
     *
     * @return список объявлений.
     */
    public List<Advert> getLastDayAdverts() {
        return this.execute(session -> session.createQuery(
                "select distinct a from Advert a "
                        + "join fetch a.user "
                        + "join fetch a.car "
                        + "left join fetch a.photos "
                        + "where a.createdOn > :minusDay", Advert.class)
                .setParameter("minusDay", Date.from(new Date(System.currentTimeMillis()).toInstant().minusSeconds(86400)))
                .list());
    }

    /**
     * Получение объявлений, имеющих фото.
     *
     * @return список объявлений.
     */
    public List<Advert> getAdvertsWithPhoto() {
        return this.execute(session -> session.createQuery(
                "select distinct a from Advert a "
                        + "join fetch a.user "
                        + "join fetch a.car "
                        + "left join fetch a.photos "
                        + "where a.photos.size > 0", Advert.class)
                .list());
    }

    /**
     * Получение объявлений, относящихся к заданной марке машины.
     *
     * @param brand марка машины.
     * @return список объявлений.
     */
    public List<Advert> getAdvertByBrandName(String brand) {
        return this.execute(session -> session.createQuery(
                "select distinct a from Advert a "
                        + "join fetch a.user "
                        + "join fetch a.car "
                        + "left join fetch a.photos "
                        + "where a.car.brand = :carBrand", Advert.class)
                .setParameter("carBrand", brand)
                .list());
    }
}