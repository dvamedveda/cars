package ru.job4j.cars.persistence;

import ru.job4j.cars.persistence.entities.Photo;

import java.util.List;

/**
 * Класс реализующий операции с сущностями Фото
 */
public class PhotoRepository implements IRepository<Photo> {

    /**
     * Получение списка фото для конкретного объявления.
     *
     * @param advertId идентификатор объявления.
     * @return список объектов фото.
     */
    public List<Photo> getPhotosByAdvertId(int advertId) {
        return this.execute(session -> session.createQuery(
                "from Photo p join fetch p.advert a where p.advert.id = :aId order by p.name asc", Photo.class)
                .setParameter("aId", advertId)
                .list()
        );
    }

    /**
     * Получение фото по идентификатору.
     *
     * @param photoId идентификатор фото.
     * @return объект фото.
     */
    public Photo getPhotoById(int photoId) {
        return this.execute(session ->
                session.createQuery("from Photo p join fetch p.advert a where p.id = :pId", Photo.class)
                        .setParameter("pId", photoId)
                        .uniqueResult()
        );
    }
}