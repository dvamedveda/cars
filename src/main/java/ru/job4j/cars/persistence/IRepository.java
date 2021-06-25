package ru.job4j.cars.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.job4j.cars.persistence.entities.IEntity;

import java.util.function.Function;

/**
 * Интерфейс, реализующий основные операции над сущностями разного типа.
 * Параметризуется типом сущности, реализующим интерфейс-маркер IEntity.
 */
public interface IRepository<T extends IEntity> {

    /**
     * Фабрика сессий для операций.
     */
    SessionFactory SESSION_FACTORY = SessionFactoryManager.getInstance().getFactory();

    /**
     * Сохранение или обновление сущности в бд.
     *
     * @param entity сущность типа IEntity
     * @return сохраненная сущность
     */
    default T saveEntity(T entity) {
        return this.execute(session -> {
            session.saveOrUpdate(entity);
            return entity;
        });
    }

    /**
     * Удаление сущности в бд.
     *
     * @param entity сущности типа IEntity.
     */
    default void deleteEntity(T entity) {
        this.execute(session -> {
            session.delete(entity);
            return true;
        });
    }

    /**
     * Декоратор для выполнения команд хибернейту.
     *
     * @param command лямбда функция для выполнения.
     * @param <G>     результат выполнения лямбды.
     * @return результат работы декоратора.
     */
    default <G> G execute(final Function<Session, G> command) {
        final Session session = SESSION_FACTORY.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            G result = command.apply(session);
            transaction.commit();
            return result;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}