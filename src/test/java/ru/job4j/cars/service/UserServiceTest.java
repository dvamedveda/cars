package ru.job4j.cars.service;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.exceptions.UnexistUserException;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса UserService.
 */
public class UserServiceTest {

    /**
     * Вспомогательный сервис.
     */
    private final UserService userService = ServiceManager.getInstance().getUserService();

    /**
     * Проверка создания пользователя.
     *
     * @throws UserAlreadyExistException в случае, если пользователь уже существует.
     */
    @Test
    public void whenCreateUserThenSuccess() throws UserAlreadyExistException {
        User user = userService.createUser("testEmail1", "testPassword1", "testUser1");
        Assert.assertThat(user.getName(), is("testUser1"));
        Assert.assertThat(user.getEmail(), is("testEmail1"));
        Assert.assertThat(user.getPassword(), is("testPassword1"));
        userService.deleteUser(user);
    }

    /**
     * Проверка поиска пользователя по электронной почте.
     *
     * @throws UnexistUserException      в случае, если пользователя не существует.
     * @throws UserAlreadyExistException в случае, если пользователь уже существует.
     */
    @Test
    public void whenSearchUserByEmailThenSuccess() throws UnexistUserException, UserAlreadyExistException {
        User user = userService.createUser("testEmail2", "testPassword2", "testUser2");
        User result = userService.getUserByEmail("testEmail2");
        Assert.assertThat(result, is(user));
        userService.deleteUser(user);
    }

    /**
     * Проверка поиска пользователя по некорректной почте.
     *
     * @throws UnexistUserException
     */
    @Test(expected = UnexistUserException.class)
    public void whenSearchUserByInvalidEmailThenException() throws UnexistUserException {
        User result = userService.getUserByEmail("testEmail3");
    }

    /**
     * Проверка создания пользователя с зарегистрированной почтой.
     *
     * @throws UnexistUserException в случае, если пользователя не существует.
     */
    @Test
    public void whenCreateUserWithExistEmailThenException() throws UnexistUserException {
        User userOne = null;
        User userTwo = null;
        try {
            userOne = userService.createUser("testEmail4", "testPassword4", "testUser4");
            userTwo = userService.createUser("testEmail4", "testPassword4", "testUser4");
        } catch (UserAlreadyExistException e) {
            System.out.println("User already exist!");
        }
        User result = userService.getUserByEmail("testEmail4");
        Assert.assertThat(result, is(userOne));
        userService.deleteUser(userOne);
    }

    /**
     * Проверка поиска пользователя по идентификатору.
     *
     * @throws UnexistUserException      в случае, если пользователь не существует.
     * @throws UserAlreadyExistException в случае, если пользователь уже существует.
     */
    @Test
    public void whenSearchUserByIdThenSuccess() throws UnexistUserException, UserAlreadyExistException {
        User user = userService.createUser("testEmail5", "testPassword5", "testUser5");
        User result = userService.getUserById(user.getId());
        Assert.assertThat(result, is(user));
        userService.deleteUser(user);
    }

    /**
     * Проверка поиска пользователя по невалидному идентификатору.
     *
     * @throws UnexistUserException в случае, если пользователя не существует.
     */
    @Test(expected = UnexistUserException.class)
    public void whenSearchUserByInvalidIdThenException() throws UnexistUserException {
        User result = userService.getUserById(123);
    }
}