package ru.job4j.cars.persistence;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.User;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса UserRepository.
 */
public class UserRepositoryTest {

    /**
     * Вспомогательное хранилище.
     */
    private final UserRepository userRepository = new UserRepository();

    /**
     * Проверка поиска пользователя по идентификатору.
     */
    @Test
    public void whenSearchUserByIdThenSuccess() {
        User user = new User("testUser1", "testEmail1", "testPassword1");
        userRepository.saveEntity(user);
        User result = userRepository.getUserById(user.getId());
        Assert.assertThat(result, is(user));
        userRepository.deleteEntity(user);
    }

    /**
     * Проверка поиска пользователя по электронной почте.
     */
    @Test
    public void whenSearchUserByEmailThenSuccess() {
        User user = new User("testUser2", "testEmail2", "testPassword2");
        userRepository.saveEntity(user);
        User result = userRepository.getUserByEmail(user.getEmail());
        Assert.assertThat(result, is(user));
        userRepository.deleteEntity(user);
    }
}