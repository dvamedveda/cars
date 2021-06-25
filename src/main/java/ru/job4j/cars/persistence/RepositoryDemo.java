package ru.job4j.cars.persistence;

import ru.job4j.cars.persistence.entities.Advert;

/**
 * Класс для демонстрации работы операций с сущностями Объявление.
 */
public class RepositoryDemo {
    public static void main(String[] args) {
        AdRepository repository = new AdRepository();
        for (Advert ad : repository.getAdvertByBrandName("Toyota")) {
            System.out.println(ad);
        }
    }
}