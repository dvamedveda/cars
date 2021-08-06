package ru.job4j.cars.persistence;

import ru.job4j.cars.persistence.entities.User;

/**
 * Класс реализующий операции с сущностями Пользователь
 */
public class UserRepository implements IRepository<User> {

    /**
     * Получение объекта пользователя по адресу его электронной почте.
     *
     * @param email адрес электронной почты.
     * @return объект пользователя.
     */
    public User getUserByEmail(String email) {
        return this.execute(session ->
                session.createQuery("from User u where u.email = :email", User.class)
                        .setParameter("email", email)
                        .uniqueResult()
        );
    }

    /**
     * Получение объекта пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект пользователя.
     */
    public User getUserById(int id) {
        return this.execute(session ->
                session.createQuery("from User u where u.id = :uId", User.class)
                        .setParameter("uId", id)
                        .uniqueResult()
        );
    }
}