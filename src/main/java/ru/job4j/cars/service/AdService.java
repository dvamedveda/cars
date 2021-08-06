package ru.job4j.cars.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import ru.job4j.cars.persistence.AdRepository;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.service.exceptions.DuplicateAdvertForCarException;

import java.util.List;

/**
 * Класс для работы с объявлениями на сервисном слое.
 */
public class AdService {

    private static final Logger LOGGER = LogManager.getLogger();
    private AdRepository repository = new AdRepository();

    /**
     * Сохранение объявления.
     *
     * @param advert объект объявления.
     * @return сохраненный объект объявления.
     * @throws DuplicateAdvertForCarException при попытке создать второе объявление для автомобиля.
     */
    public Advert saveAdvert(Advert advert) throws DuplicateAdvertForCarException {
        Advert result = null;
        try {
            result = this.repository.saveEntity(advert);
        } catch (ConstraintViolationException e) {
            throw new DuplicateAdvertForCarException("This car already has own advert!");
        }
        return result;
    }

    /**
     * Удаление объявления.
     *
     * @param advert объект объявления.
     */
    public void deleteAdvert(Advert advert) {
        this.repository.deleteEntity(advert);
    }

    /**
     * Получить список опубликованных объявлений, созданных за последний день.
     *
     * @return список объявлений.
     */
    public List<Advert> getLastDayAdverts() {
        return this.repository.getPublishedLastDayAdverts();
    }

    /**
     * Получить список опубликованных объявлений по марке автомобиля.
     *
     * @param brand марка автомобиля.
     * @return список объявлений.
     */
    public List<Advert> getAdvertsByBrandName(String brand) {
        return this.repository.getPublishedAdvertByBrandName(brand);
    }

    /**
     * Получить список всех опубликованных объявлений, имеющих фото.
     *
     * @return список объявлений.
     */
    public List<Advert> getAdvertsWithPhoto() {
        return this.repository.getPublishedAdvertsWithPhoto();
    }

    /**
     * Получить список всех опубликованных объявлений.
     *
     * @return список объявлений.
     */
    public List<Advert> getAllAdverts() {
        return this.repository.getPublishedAllAdverts();
    }

    /**
     * Сериализовать список объявлений в JSON.
     *
     * @param list список объявлений.
     * @return строка в формате JSON.
     */
    public static String asJson(List<Advert> list) {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(list);
        } catch (Exception e) {
            LOGGER.warn(e, e);
        }
        return result;
    }

    /**
     * Получить объявление по идентификатору.
     *
     * @param id идентификатор объявления.
     * @return объект объявления.
     */
    public Advert getAdvertById(int id) {
        return this.repository.getAdvertById(id);
    }

    /**
     * Получить список объявлений, принадлежащих пользователю.
     *
     * @param id идентификатор пользователя.
     * @return список объявлений.
     */
    public List<Advert> getAdvertsByUserId(int id) {
        return this.repository.getAdvertsByUserId(id);
    }
}