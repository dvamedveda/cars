package ru.job4j.cars.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.persistence.PhotoRepository;
import ru.job4j.cars.persistence.entities.Photo;

import java.util.List;

/**
 * Класс для работы с фото на сервисном слое.
 */
public class PhotoService {

    private static final Logger LOGGER = LogManager.getLogger();
    private PhotoRepository repository = new PhotoRepository();

    /**
     * Сохранение фото.
     *
     * @param photo объект фото.
     * @return сохраненный объект фото.
     */
    public Photo addPhoto(Photo photo) {
        return this.repository.saveEntity(photo);
    }

    /**
     * Удаление фото.
     *
     * @param photo объект фото.
     */
    public void removePhoto(Photo photo) {
        this.repository.deleteEntity(photo);
    }

    /**
     * Получить список фото для конкретного объявления.
     *
     * @param id идентификатор объявления.
     * @return список фото.
     */
    public List<Photo> getPhotoByAdvertId(int id) {
        return this.repository.getPhotosByAdvertId(id);
    }

    /**
     * Получить фото по идентификатору.
     *
     * @param id идентификатор фото.
     * @return объект фото.
     */
    public Photo getPhotoById(int id) {
        return this.repository.getPhotoById(id);
    }
}