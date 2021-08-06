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
 * Тесты класса PhotoRepository.
 */
public class PhotoRepositoryTest {

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
    private final PhotoRepository photoRepository = new PhotoRepository();

    /**
     * Проверка поиска фото в хранилище по идентификатору.
     */
    @Test
    public void whenSearchPhotoByIdThenSuccess() {
        Date now = new Date(System.currentTimeMillis());
        Car car = new Car("Lada", "Priora", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        carRepository.saveEntity(car);
        User user = new User("testUser1", "testEmail1", "testPassword1");
        userRepository.saveEntity(user);
        Advert adOne = new Advert("Test lada ad", "Lada ad desc", user, car, Date.from(now.toInstant().minusSeconds(86401)), 10);
        adOne.setPublished(true);
        adRepository.saveEntity(adOne);
        Photo photoOne = new Photo("test lada photo one", new byte[1], adOne);
        Photo photoTwo = new Photo("test lada photo two", new byte[1], adOne);
        adOne.addPhoto(photoOne);
        adOne.addPhoto(photoTwo);
        adRepository.saveEntity(adOne);
        Photo resultOne = photoRepository.getPhotoById(photoOne.getId());
        Assert.assertThat(resultOne, is(photoOne));
        Photo resultTwo = photoRepository.getPhotoById(photoTwo.getId());
        Assert.assertThat(resultTwo, is(photoTwo));
        adRepository.deleteEntity(adOne);
        carRepository.deleteEntity(car);
        userRepository.deleteEntity(user);
    }

    /**
     * Проверка поиска фото в хранилище по идентификатору объявления.
     */
    @Test
    public void whenSearchPhotoByAdvertIdThenSuccess() {
        Date now = new Date(System.currentTimeMillis());
        Car car = new Car("Lada", "Niva", "Jeep", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        carRepository.saveEntity(car);
        User user = new User("testUser2", "testEmail2", "testPassword2");
        userRepository.saveEntity(user);
        Advert adOne = new Advert("Test niva ad", "Niva ad desc", user, car, Date.from(now.toInstant().minusSeconds(86401)), 10);
        adOne.setPublished(true);
        adRepository.saveEntity(adOne);
        Photo photoOne = new Photo("test niva photo 1", new byte[1], adOne);
        Photo photoTwo = new Photo("test niva photo 2", new byte[1], adOne);
        adOne.addPhoto(photoOne);
        adOne.addPhoto(photoTwo);
        adRepository.saveEntity(adOne);
        List<Photo> result = photoRepository.getPhotosByAdvertId(adOne.getId());
        Assert.assertThat(result.size(), is(2));
        Assert.assertThat(result.get(0), is(photoOne));
        Assert.assertThat(result.get(1), is(photoTwo));
        adRepository.deleteEntity(adOne);
        carRepository.deleteEntity(car);
        userRepository.deleteEntity(user);
    }
}