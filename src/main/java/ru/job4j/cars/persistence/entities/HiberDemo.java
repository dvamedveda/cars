package ru.job4j.cars.persistence.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Date;

/**
 * Демонстрация работы с сущностями Объявление, Автомобиль, Пользователь, Фото.
 */
public class HiberDemo {
    private static final long YEAR = 31536000;

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        try {
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            User user = new User("Boris", "email", "password");
            session.save(user);
            Date produced = Date.from(new Date(System.currentTimeMillis()).toInstant().minusSeconds(YEAR * 10));
            Car car1 = new Car("Honda", "Civic", "Sedan", produced);
            Car car2 = new Car("Toyota", "Prius", "Sedan", produced);
            session.save(car1);
            session.save(car2);

            Date now = new Date(System.currentTimeMillis());
            Advert advert1 = new Advert(
                    "Test ad",
                    "Ad description",
                    user, car1, now, 100500);
            Advert advert2 = new Advert(
                    "Test ad 2",
                    "Ad 2 description",
                    user, car2, now, 9999);
            session.save(advert1);
            session.save(advert2);
            Photo photoOne = new Photo("photo name 1", new byte[1], advert1);
            Photo photoTwo = new Photo("photo name 2", new byte[1], advert1);
            advert1.addPhoto(photoOne);
            advert1.addPhoto(photoTwo);
            session.save(advert1);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}