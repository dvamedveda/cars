package ru.job4j.cars.persistence;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.Photo;
import ru.job4j.cars.persistence.entities.User;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса AdRepository.
 */
public class AdRepositoryTest {

    /**
     * Константа, представляющая год в секундах.
     */
    private static final long YEAR = 31536000;

    /**
     * Вспомогательные хранилища.
     */
    private final UserRepository userRepository = new UserRepository();
    private final CarRepository carRepository = new CarRepository();
    private final AdRepository adRepository = new AdRepository();

    /**
     * Проверка поиска опубликованных объявлений, созданных за последние сутки.
     */
    @Test
    public void whenSearchPublishedAdsForLastDayThenSuccess() {
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Honda", "Civic", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 5)));
        Car car2 = new Car("Toyota", "Prius", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 10)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User user = new User("Boris", "email", "password");
        userRepository.saveEntity(user);
        Advert adOne = new Advert("Test ad one", "One ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Test ad two", "Two ad desc", user, car2, now, 45);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        List<Advert> result = adRepository.getPublishedLastDayAdverts();
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(adTwo));
        adRepository.deleteEntity(adOne);
        adRepository.deleteEntity(adTwo);
        carRepository.deleteEntity(car1);
        carRepository.deleteEntity(car2);
        userRepository.deleteEntity(user);
    }

    /**
     * Проверка поиска  опубликованных объявлений, имеющих фото.
     */
    @Test
    public void whenSearchPublishedAdsWithFotoThenSuccess() {
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Peugeot", "306", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        Car car2 = new Car("Volvo", "X70", "Hatchback", Date.from(now.toInstant().minusSeconds(YEAR * 7)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User user = new User("Boris", "email", "password");
        userRepository.saveEntity(user);
        Advert adOne = new Advert("Test ad one", "One ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Test ad two", "Two ad desc", user, car2, now, 45);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        Photo photoOne = new Photo("test photo one", new byte[1], adOne);
        Photo photoTwo = new Photo("test photo two", new byte[1], adOne);
        adOne.addPhoto(photoOne);
        adOne.addPhoto(photoTwo);
        adRepository.saveEntity(adOne);
        List<Advert> result = adRepository.getPublishedAdvertsWithPhoto();
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
     * Проверка поиска опубликованных объявлений с указанным брендом.
     */
    @Test
    public void whenSearchPublishedByBrandThenSuccess() {
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("KAMAZ", "800", "Cargo", Date.from(now.toInstant().minusSeconds(YEAR * 5)));
        Car car2 = new Car("Nissan", "Almera", "Universal", Date.from(now.toInstant().minusSeconds(YEAR * 10)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User userOne = new User("Boris", "email1", "password1");
        User userTwo = new User("Ivan", "email2", "password2");
        userRepository.saveEntity(userOne);
        userRepository.saveEntity(userTwo);
        Advert adOne = new Advert("Test ad kamaz", "One ad kamaz desc", userOne, car1, now, 1000);
        Advert adTwo = new Advert("Test ad nissan", "Two ad nissan desc", userTwo, car2, now, 88);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        List<Advert> result = adRepository.getPublishedAdvertByBrandName("Nissan");
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

    /**
     * Проверка поиска всех опубликованных объявлений.
     */
    @Test
    public void whenSearchAllPublishedThenSuccess() {
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("BMW", "3", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        Car car2 = new Car("Toyota", "Estima", "Miniwan", Date.from(now.toInstant().minusSeconds(YEAR * 2)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User userOne = new User("Nikolay", "email1", "password1");
        User userTwo = new User("Vasiliy", "email2", "password2");
        userRepository.saveEntity(userOne);
        userRepository.saveEntity(userTwo);
        Advert adOne = new Advert("Test ad bmw", "One ad bmw desc", userOne, car1, Date.from(now.toInstant().minusSeconds(10)), 1000);
        Advert adTwo = new Advert("Test ad toyota", "Two ad toyota desc", userTwo, car2, now, 88);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        List<Advert> result = adRepository.getPublishedAllAdverts();
        Assert.assertThat(result.size(), is(2));
        Assert.assertThat(result.get(0), is(adTwo));
        Assert.assertThat(result.get(0).getCar(), is(car2));
        Assert.assertThat(result.get(1), is(adOne));
        Assert.assertThat(result.get(1).getCar(), is(car1));
        adRepository.deleteEntity(adOne);
        adRepository.deleteEntity(adTwo);
        carRepository.deleteEntity(car1);
        carRepository.deleteEntity(car2);
        userRepository.deleteEntity(userOne);
        userRepository.deleteEntity(userTwo);
    }

    /**
     * Проверка поиска объявления по идентификатору.
     */
    @Test
    public void whenSearchAdByIdThenSuccess() {
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Honda", "Accord", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 4)));
        Car car2 = new Car("Honda", "Civic", "Miniwan", Date.from(now.toInstant().minusSeconds(YEAR * 6)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User userOne = new User("Damir", "email1", "password1");
        User userTwo = new User("Erlan", "email2", "password2");
        userRepository.saveEntity(userOne);
        userRepository.saveEntity(userTwo);
        Advert adOne = new Advert("Test ad honda one", "One ad honda desc", userOne, car1, Date.from(now.toInstant().minusSeconds(10)), 1000);
        Advert adTwo = new Advert("Test ad honda two", "Two ad honda desc", userTwo, car2, now, 88);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        Advert resultOne = adRepository.getAdvertById(adOne.getId());
        Assert.assertThat(resultOne, is(adOne));
        Advert resultTwo = adRepository.getAdvertById(adTwo.getId());
        Assert.assertThat(resultTwo, is(adTwo));
        adRepository.deleteEntity(adOne);
        adRepository.deleteEntity(adTwo);
        carRepository.deleteEntity(car1);
        carRepository.deleteEntity(car2);
        userRepository.deleteEntity(userOne);
        userRepository.deleteEntity(userTwo);
    }

    @Test
    public void whenSearchUserTasksThenCorrect() {
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Honda", "Civic", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 5)));
        Car car2 = new Car("Chevrolet", "Corvette", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 10)));
        carRepository.saveEntity(car1);
        carRepository.saveEntity(car2);
        User user = new User("testUser3", "testEmail3", "testPassword3");
        userRepository.saveEntity(user);
        Advert adOne = new Advert("Test ad one", "One ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Test ad two", "Two ad desc", user, car2, now, 45);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adRepository.saveEntity(adOne);
        adRepository.saveEntity(adTwo);
        List<Advert> result = adRepository.getAdvertsByUserId(user.getId());
        Assert.assertThat(result.size(), is(2));
        Assert.assertThat(result.get(0), is(adTwo));
        Assert.assertThat(result.get(1), is(adOne));
        adRepository.deleteEntity(adOne);
        adRepository.deleteEntity(adTwo);
        carRepository.deleteEntity(car1);
        carRepository.deleteEntity(car2);
        userRepository.deleteEntity(user);
    }
}