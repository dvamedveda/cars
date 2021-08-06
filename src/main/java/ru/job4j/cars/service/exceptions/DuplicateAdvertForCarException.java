package ru.job4j.cars.service.exceptions;

/**
 * Исключение, бросающееся в ситуации, когда искомый пользователь не найден в бд.
 */
public class DuplicateAdvertForCarException extends Exception {

    public DuplicateAdvertForCarException(String message) {
        super(message);
    }
}