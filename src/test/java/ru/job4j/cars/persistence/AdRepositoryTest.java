package ru.job4j.cars.persistence;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.Photo;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.persistence.store.DatabaseUpdater;
import ru.job4j.cars.persistence.store.StoreSettings;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса AdRepository.
 */
public class AdRepositoryTest {
    private static final long YEAR = 31536000;

    /**
     * Подготовить базу данных перед тестами.
     */
    @BeforeClass
    public static void setUp() {
        DatabaseUpdater databaseUpdater = new DatabaseUpdater(StoreSettings.TEST_DB_FILE);
        databaseUpdater.updateDatabase();
    }

    /**
     * Проверка поиска объявлений, созданных за последние сутки.
     */
    @Test
    public void whenSearchAdsForLastDayThenSuccess() {
        UserRepository userRepository = new UserRepository();
        CarRepository carRepository = new CarRepository();
        AdRepository adRepository = new AdRepository();
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Honda", "Civic", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 5)));
        Car car2 = new Car("Toyota", "Prius", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 10)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User user = new User("Boris", now);
        userRepository.saveEntity(user);
        Advert adOne = new Advert("Test ad one", "One ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Test ad two", "Two ad desc", user, car2, now, 45);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        List<Advert> result = adRepository.getLastDayAdverts();
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(adTwo));
        adRepository.deleteEntity(adOne);
        adRepository.deleteEntity(adTwo);
        carRepository.deleteEntity(car1);
        carRepository.deleteEntity(car2);
        userRepository.deleteEntity(user);
    }

    /**
     * Проверка поиска объявлений, имеющих фото.
     */
    @Test
    public void whenSearchAdsWithFotoThenSuccess() {
        UserRepository userRepository = new UserRepository();
        CarRepository carRepository = new CarRepository();
        AdRepository adRepository = new AdRepository();
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Peugeot", "306", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        Car car2 = new Car("Volvo", "X70", "Hatchback", Date.from(now.toInstant().minusSeconds(YEAR * 7)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User user = new User("Boris", now);
        userRepository.saveEntity(user);
        Advert adOne = new Advert("Test ad one", "One ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Test ad two", "Two ad desc", user, car2, now, 45);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        Photo photoOne = new Photo("test photo one", "some one photo data", adOne);
        Photo photoTwo = new Photo("test photo two", "some two photo data", adOne);
        adOne.addPhoto(photoOne);
        adOne.addPhoto(photoTwo);
        adRepository.saveEntity(adOne);
        List<Advert> result = adRepository.getAdvertsWithPhoto();
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(adOne));
        Assert.assertThat(result.get(0).getPhotos().size(), is(2));
        Assert.assertThat(result.get(0).getPhotos().contains(photoOne), is(true));
        Assert.assertThat(result.get(0).getPhotos().contains(photoTwo), is(true));
        adRepository.deleteEntity(adOne);
        adRepository.deleteEntity(adTwo);
        carRepository.deleteEntity(car1);
        carRepository.deleteEntity(car2);
        userRepository.deleteEntity(user);
    }

    /**
     * Проверка поиска объявлений с указанным брендом.
     */
    @Test
    public void whenSearchByBrandThenSuccess() {
        UserRepository userRepository = new UserRepository();
        CarRepository carRepository = new CarRepository();
        AdRepository adRepository = new AdRepository();
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("KAMAZ", "800", "Cargo", Date.from(now.toInstant().minusSeconds(YEAR * 5)));
        Car car2 = new Car("Nissan", "Almera", "Universal", Date.from(now.toInstant().minusSeconds(YEAR * 10)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User userOne = new User("Boris", now);
        User userTwo = new User("Ivan", now);
        userRepository.saveEntity(userOne);
        userRepository.saveEntity(userTwo);
        Advert adOne = new Advert("Test ad kamaz", "One ad kamaz desc", userOne, car1, now, 1000);
        Advert adTwo = new Advert("Test ad nissan", "Two ad nissan desc", userTwo, car2, now, 88);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        List<Advert> result = adRepository.getAdvertByBrandName("Nissan");
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(adTwo));
        Assert.assertThat(result.get(0).getCar(), is(car2));
        adRepository.deleteEntity(adOne);
        adRepository.deleteEntity(adTwo);
        carRepository.deleteEntity(car1);
        carRepository.deleteEntity(car2);
        userRepository.deleteEntity(userOne);
        userRepository.deleteEntity(userTwo);
    }
}