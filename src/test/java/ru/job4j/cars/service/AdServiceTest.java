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
 * Тесты класса AdService.
 */
public class AdServiceTest {

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

    /**
     * Проверка получения всех объявлений.
     *
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     */
    @Test
    public void whenGetAllAdvertsThenSuccess() throws DuplicateAdvertForCarException, UserAlreadyExistException {
        User userOne = userService.createUser("testEmail1", "testPassword1", "testUser1");
        User userTwo = userService.createUser("testEmail2", "testPassword2", "testUser2");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Lada", "Niva", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 2)));
        Car car2 = new Car("Lada", "Kalina", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 10)));
        carService.saveCar(car1);
        carService.saveCar(car2);
        Advert adOne = new Advert("Lada ad one", "One lada ad desc", userOne, car1, Date.from(now.toInstant().minusSeconds(86400 * 2)), 10);
        Advert adTwo = new Advert("Lada ad two", "Two lada ad desc", userTwo, car2, now, 45);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adService.saveAdvert(adOne);
        adService.saveAdvert(adTwo);
        List<Advert> result = adService.getAllAdverts();
        Assert.assertThat(result.size(), is(2));
        Assert.assertThat(result.get(0), is(adTwo));
        Assert.assertThat(result.get(1), is(adOne));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }

    /**
     * Проверка получения объявлений за прошедший день.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenGetLastDayAdvertsThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User userOne = userService.createUser("testEmail3", "testPassword3", "testUser3");
        User userTwo = userService.createUser("testEmail4", "testPassword4", "testUser4");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Lada", "Priora", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        Car car2 = new Car("Lada", "09", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 3)));
        carService.saveCar(car1);
        carService.saveCar(car2);
        Advert adOne = new Advert("Lada ad one", "One lada ad desc", userOne, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Lada ad two", "Two lada ad desc", userTwo, car2, now, 77);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adService.saveAdvert(adOne);
        adService.saveAdvert(adTwo);
        List<Advert> result = adService.getLastDayAdverts();
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(adTwo));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }

    /**
     * Проверка получения объявлений с заданной маркой.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenGetAdvertsByBrandThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User user = userService.createUser("testEmail5", "testPassword5", "testUser5");
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
        List<Advert> result = adService.getAdvertsByBrandName("Subaru");
        Assert.assertThat(result.size(), is(1));
        Assert.assertThat(result.get(0), is(adOne));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(user);
    }

    /**
     * Проверка получения объявления по идентификатору.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenGetAdvertByIdThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User user = userService.createUser("testEmail6", "testPassword6", "testUser6");
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
        Advert result = adService.getAdvertById(adTwo.getId());
        Assert.assertThat(result, is(adTwo));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(user);
    }

    /**
     * Проверка получения объявлений с фото.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenGetAdvertWithPhotoThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException {
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
     * Проверка сериализации списка объявлений в формат JSON.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenJsonUserTasksThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User userOne = userService.createUser("testEmail8", "testPassword8", "testUser8");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Honda", "Inspire", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 5)));
        carService.saveCar(car1);
        Advert adOne = new Advert("Test ad inspire", "Inspire ad desc", userOne, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        adOne.setPublished(true);
        adService.saveAdvert(adOne);
        String result = adService.asJson(adService.getAdvertsByUserId(userOne.getId()));
        String expected = new StringBuilder()
                .append("[{")
                .append("\"id\":").append(adOne.getId()).append(",")
                .append("\"summary\":\"").append(adOne.getSummary()).append("\",")
                .append("\"description\":\"").append(adOne.getDescription()).append("\",")
                .append("\"closed\":").append(adOne.isClosed()).append(",")
                .append("\"car\":").append("{")
                .append("\"id\":").append(adOne.getCar().getId()).append(",")
                .append("\"brand\":\"").append(adOne.getCar().getBrand()).append("\",")
                .append("\"model\":\"").append(adOne.getCar().getModel()).append("\",")
                .append("\"body\":\"").append(adOne.getCar().getBody()).append("\",")
                .append("\"produced\":").append(adOne.getCar().getProduced().getTime()).append("},")
                .append("\"createdOn\":").append(adOne.getCreatedOn().getTime()).append(",")
                .append("\"closedOn\":").append(adOne.getClosedOn().getTime()).append(",")
                .append("\"published\":").append(adOne.isPublished()).append(",")
                .append("\"price\":").append(adOne.getPrice()).append(",")
                .append("\"photos\":").append(adOne.getPhotos())
                .append("}]")
                .toString();
        Assert.assertThat(result, is(expected));
        adService.deleteAdvert(adOne);
        carService.removeCar(car1);
        userService.deleteUser(userOne);
    }

    /**
     * Проверка получения объявлений пользователя, по идентификатору пользователя.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     */
    @Test
    public void whenGetUserAdvertsThenSuccess() throws UserAlreadyExistException, DuplicateAdvertForCarException {
        User userOne = userService.createUser("testEmail6", "testPassword6", "testUser6");
        User userTwo = userService.createUser("testEmail7", "testPassword7", "testUser7");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("Honda", "Civic", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 5)));
        Car car2 = new Car("Toyota", "Prius", "Sedan", Date.from(now.toInstant().minusSeconds(YEAR * 10)));
        carService.saveCar(car1);
        carService.saveCar(car2);
        Advert adOne = new Advert("Test ad one", "One ad desc", userOne, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("Test ad two", "Two ad desc", userTwo, car2, now, 45);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        adService.saveAdvert(adOne);
        adService.saveAdvert(adTwo);
        List<Advert> resultOne = adService.getAdvertsByUserId(userOne.getId());
        Assert.assertThat(resultOne.size(), is(1));
        Assert.assertThat(resultOne.get(0), is(adOne));
        List<Advert> resultTwo = adService.getAdvertsByUserId(userTwo.getId());
        Assert.assertThat(resultTwo.size(), is(1));
        Assert.assertThat(resultTwo.get(0), is(adTwo));
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        carService.removeCar(car2);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }

    /**
     * Проверка невозможности создать два объявления для одной машины.
     *
     * @throws UserAlreadyExistException в случае, если пользователь уже существует.
     */
    @Test
    public void whenCreateTwoAdvertForOneCarThenException() throws UserAlreadyExistException {
        User user = userService.createUser("testEmail8", "testPassword8", "testUser8");
        Date now = new Date(System.currentTimeMillis());
        Car car1 = new Car("ZIL", "500", "Cargo", Date.from(now.toInstant().minusSeconds(YEAR * 4)));
        carService.saveCar(car1);
        Advert adOne = new Advert("ZIL ad one", "One zil ad desc", user, car1, Date.from(now.toInstant().minusSeconds(86401)), 10);
        Advert adTwo = new Advert("ZIL ad two", "Two zil ad desc", user, car1, now, 77);
        adOne.setPublished(true);
        adTwo.setPublished(true);
        try {
            adService.saveAdvert(adOne);
            adService.saveAdvert(adTwo);
        } catch (DuplicateAdvertForCarException e) {
            e.printStackTrace();
        }
        adService.deleteAdvert(adOne);
        adService.deleteAdvert(adTwo);
        carService.removeCar(car1);
        userService.deleteUser(user);
    }
}