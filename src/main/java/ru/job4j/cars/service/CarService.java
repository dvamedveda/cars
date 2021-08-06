package ru.job4j.cars.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.persistence.CarRepository;
import ru.job4j.cars.persistence.entities.Car;

/**
 * Класс для работы с автомобилями на сервисном слое.
 */
public class CarService {
    private static final Logger LOGGER = LogManager.getLogger();
    private CarRepository repository = new CarRepository();

    /**
     * Сохранить автомобиль.
     *
     * @param car объект автомобиля.
     * @return сохраненный объект автомобиля.
     */
    public Car saveCar(Car car) {
        return this.repository.saveEntity(car);
    }

    /**
     * Удаление автомобиля.
     *
     * @param car объект автомобиля.
     */
    public void removeCar(Car car) {
        this.repository.deleteEntity(car);
    }
}