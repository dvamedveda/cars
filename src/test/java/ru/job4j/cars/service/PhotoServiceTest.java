package ru.job4j.cars.service;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.Photo;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.exceptions.DuplicateAdvertForCarException;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса PhotoService.
 */
public class PhotoServiceTest {

    /**
     * Константа, представляющая год в секундах.
     */
    private static final long YEAR = 31536000;

    /**
     * Вспомогательные сервисы.
     */
    private final UserService userService = ServiceManager.getInstance().getUserService();
    private final CarService carService = ServiceManager.getInstance().getCarService();
    private final AdService adService = ServiceManager.getInstance().getAdService();
    private final PhotoService photoService = ServiceManager.getInstance().getPhotoService();

    /**
     * Проверка сохранения фото для объявления.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenAddPhotoThenSuccess() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User user = userService.createUser("testEmail7", "testPassword7", "testUser7");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Subaru", "Outback", "Universal", Date.from(now.toInstant().minusSeconds(YEAR * 4)));
        Car car2 = new Car("Lada", "09", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        carService.saveCar(car1);
        carService.saveCar(car2);
        Advert adOne = new Advert("Subaru ad one", "One subaru ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Lada ad two", "Two lada ad desc", user, car2, now, 77);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adService.saveAdvert(adOne);
        adService.saveAdvert(adTwo);
        Photo photo = new Photo("testPhoto1", new byte[1], adOne);
        photoService.addPhoto(photo);
        adOne = adService.getAdvertById(adOne.getId());
        List<Advert> result = adService.getAdvertsWithPhoto();
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(adOne));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(user);
    }

    /**
     * Проверка удаления фото.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenRemovePhotoThenSuccess() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User user = userService.createUser("testEmail7", "testPassword7", "testUser7");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Subaru", "Outback", "Universal", Date.from(now.toInstant().minusSeconds(YEAR * 4)));
        Car car2 = new Car("Lada", "09", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        carService.saveCar(car1);
        carService.saveCar(car2);
        Advert adOne = new Advert("Subaru ad one", "One subaru ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Lada ad two", "Two lada ad desc", user, car2, now, 77);
        Photo photo = new Photo("testPhoto1", new byte[1], adOne);
        adOne.addPhoto(photo);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adService.saveAdvert(adOne);
        adService.saveAdvert(adTwo);
        photoService.removePhoto(photo);
        adOne = adService.getAdvertById(adOne.getId());
        List<Advert> result = adService.getAllAdverts();
        Assert.assertThat(result.size(), is(2));
        Assert.assertThat(result.get(0), is(adTwo));
        Assert.assertThat(result.get(1), is(adOne));
        List<Advert> withPhotoResult = adService.getAdvertsWithPhoto();
        Assert.assertThat(withPhotoResult.size(), is(0));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(user);
    }

    /**
     * Проверка получения фото по идентификатору.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenGetPhotoByIdThenSuccess() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User user = userService.createUser("testEmail7", "testPassword7", "testUser7");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Subaru", "Outback", "Universal", Date.from(now.toInstant().minusSeconds(YEAR * 4)));
        Car car2 = new Car("Lada", "09", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        carService.saveCar(car1);
        carService.saveCar(car2);
        Advert adOne = new Advert("Subaru ad one", "One subaru ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Lada ad two", "Two lada ad desc", user, car2, now, 77);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adService.saveAdvert(adOne);
        adService.saveAdvert(adTwo);
        Photo photo = photoService.addPhoto(new Photo("testPhoto1", new byte[1], adOne));
        adOne = adService.getAdvertById(adOne.getId());
        Photo result = photoService.getPhotoById(photo.getId());
        Assert.assertThat(result, is(photo));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(user);
    }

    /**
     * Проверка получения фото для оъявления, по идентификатору.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenGetAdvertPhotosByAdvertIdThenSuccess() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User user = userService.createUser("testEmail7", "testPassword7", "testUser7");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Subaru", "Outback", "Universal", Date.from(now.toInstant().minusSeconds(YEAR * 4)));
        Car car2 = new Car("Lada", "09", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        carService.saveCar(car1);
        carService.saveCar(car2);
        Advert adOne = new Advert("Subaru ad one", "One subaru ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Lada ad two", "Two lada ad desc", user, car2, now, 77);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adService.saveAdvert(adOne);
        adService.saveAdvert(adTwo);
        Photo photo = photoService.addPhoto(new Photo("testPhoto1", new byte[1], adOne));
        adOne = adService.getAdvertById(adOne.getId());
        List<Photo> result = photoService.getPhotoByAdvertId(adOne.getId());
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(photo));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(user);
    }
}