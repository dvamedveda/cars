package ru.job4j.cars.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import ru.job4j.cars.persistence.UserRepository;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.exceptions.UnexistUserException;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

/**
 * Класс для работы с пользователями на сервисном слое.
 */
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger();
    private UserRepository repository = new UserRepository();

    /**
     * Создать пользователя.
     *
     * @param email    электронная почта пользователя.
     * @param password пароль пользователя.
     * @param name     имя пользователя.
     * @return объект созданного пользователя.
     * @throws UserAlreadyExistException в случае, если такой пользователь уже существует.
     */
    public User createUser(String email, String password, String name) throws UserAlreadyExistException {
        User result = null;
        try {
            result = this.repository.saveEntity(new User(name, email, password));
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistException("User with email " + email + " already exists!");
        }
        return result;
    }

    /**
     * Удаление пользователя.
     *
     * @param user объект пользователя для удаления.
     */
    public void deleteUser(User user) {
        this.repository.deleteEntity(user);
    }

    /**
     * Получение объекта пользователя по адресу электронной почты.
     *
     * @param email электронная почта.
     * @return объект пользователя.
     * @throws UnexistUserException в случае, если пользователь не существует.
     */
    public User getUserByEmail(String email) throws UnexistUserException {
        User result = this.repository.getUserByEmail(email);
        if (result != null) {
            return result;
        } else {
            throw new UnexistUserException("User with email " + email + " not exists!");
        }
    }

    /**
     * Получение объекта пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект пользователя.
     * @throws UnexistUserException в случае, если пользователя не существует.
     */
    public User getUserById(int id) throws UnexistUserException {
        User result = this.repository.getUserById(id);
        if (result != null) {
            return result;
        } else {
            throw new UnexistUserException("User with id " + id + " not exists!");
        }
    }
}