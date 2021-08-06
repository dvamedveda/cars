package ru.job4j.cars.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.Photo;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.AdService;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.ServiceManager;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.service.exceptions.DuplicateAdvertForCarException;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса SearchServlet.
 */
public class SearchServletTest {

    /**
     * Вспомогательные сервисы и переменные.
     */
    private final UserService userService = ServiceManager.getInstance().getUserService();
    private final AdService adService = ServiceManager.getInstance().getAdService();
    private final CarService carService = ServiceManager.getInstance().getCarService();
    private final Date now = new Date(System.currentTimeMillis());

    /**
     * Проверка получения данных о всех объявлениях.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws IOException                    исключения ввода и вывода.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenSearchDefaultThenAll() throws UserAlreadyExistException, DuplicateAdvertForCarException, IOException, ServletException {
        User userOne = userService.createUser("some1@email", "somepass1", "somename1");
        User userTwo = userService.createUser("some2@email", "somepass2", "somename3");
        Car carOne = carService.saveCar(new Car("Dodge", "Chalenger", "Sedan", new Date(1L)));
        Car carTwo = carService.saveCar(new Car("Chevrolet", "Lacetti", "Sedan", new Date(1L)));
        Advert advertOne = adService.saveAdvert(new Advert("Dodge ad", "Dodge ad desc", userOne, carOne, now, 1));
        Advert advertTwo = adService.saveAdvert(new Advert("Chevrolet ad", "Chevrolet ad desc", userTwo, carTwo, Date.from(now.toInstant().minusSeconds(86401)), 1));
        Photo photo = new Photo("name", "data".getBytes(), advertOne);
        advertOne.addPhoto(photo);
        advertOne.setPublished(true);
        advertTwo.setPublished(true);
        adService.saveAdvert(advertOne);
        adService.saveAdvert(advertTwo);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        StringWriter testOut = new StringWriter();
        PrintWriter writer = new PrintWriter(testOut);
        Mockito.when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        ObjectMapper mapper = new ObjectMapper();
        String expected = new StringBuilder()
                .append("[{")
                .append("\"id\":").append(advertOne.getId()).append(",")
                .append("\"summary\":\"").append(advertOne.getSummary()).append("\",")
                .append("\"description\":\"").append(advertOne.getDescription()).append("\",")
                .append("\"closed\":").append(advertOne.isClosed()).append(",")
                .append("\"car\":").append("{")
                .append("\"id\":").append(advertOne.getCar().getId()).append(",")
                .append("\"brand\":\"").append(advertOne.getCar().getBrand()).append("\",")
                .append("\"model\":\"").append(advertOne.getCar().getModel()).append("\",")
                .append("\"body\":\"").append(advertOne.getCar().getBody()).append("\",")
                .append("\"produced\":").append(advertOne.getCar().getProduced().getTime()).append("},")
                .append("\"createdOn\":").append(advertOne.getCreatedOn().getTime()).append(",")
                .append("\"closedOn\":").append(advertOne.getClosedOn().getTime()).append(",")
                .append("\"published\":").append(advertOne.isPublished()).append(",")
                .append("\"price\":").append(advertOne.getPrice()).append(",")
                .append("\"photos\":[{")
                .append("\"id\":").append(advertOne.getPhotos().get(0).getId()).append(",")
                .append("\"name\":\"").append(advertOne.getPhotos().get(0).getName()).append("\",")
                .append("\"data\":").append(mapper.writeValueAsString(advertOne.getPhotos().get(0).getData()))
                .append("}]")
                .append("},{")
                .append("\"id\":").append(advertTwo.getId()).append(",")
                .append("\"summary\":\"").append(advertTwo.getSummary()).append("\",")
                .append("\"description\":\"").append(advertTwo.getDescription()).append("\",")
                .append("\"closed\":").append(advertTwo.isClosed()).append(",")
                .append("\"car\":").append("{")
                .append("\"id\":").append(advertTwo.getCar().getId()).append(",")
                .append("\"brand\":\"").append(advertTwo.getCar().getBrand()).append("\",")
                .append("\"model\":\"").append(advertTwo.getCar().getModel()).append("\",")
                .append("\"body\":\"").append(advertTwo.getCar().getBody()).append("\",")
                .append("\"produced\":").append(advertTwo.getCar().getProduced().getTime()).append("},")
                .append("\"createdOn\":").append(advertTwo.getCreatedOn().getTime()).append(",")
                .append("\"closedOn\":").append(advertTwo.getClosedOn().getTime()).append(",")
                .append("\"published\":").append(advertTwo.isPublished()).append(",")
                .append("\"price\":").append(advertTwo.getPrice()).append(",")
                .append("\"photos\":").append(advertTwo.getPhotos())
                .append("}]")
                .toString();
        new SearchServlet().doGet(req, resp);
        String result = testOut.toString();
        Assert.assertThat(result, is(expected));
        adService.deleteAdvert(advertOne);
        adService.deleteAdvert(advertTwo);
        carService.removeCar(carOne);
        carService.removeCar(carTwo);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }

    /**
     * Проверка получения данных об объявлениях за прошедший день.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws IOException                    исключения ввода и вывода.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenSearchLastdayThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException, IOException, ServletException {
        User userOne = userService.createUser("some1@email", "somepass1", "somename1");
        User userTwo = userService.createUser("some2@email", "somepass2", "somename3");
        Car carOne = carService.saveCar(new Car("Dodge", "Chalenger", "Sedan", new Date(1L)));
        Car carTwo = carService.saveCar(new Car("Chevrolet", "Lacetti", "Sedan", new Date(1L)));
        Advert advertOne = adService.saveAdvert(new Advert("Dodge ad", "Dodge ad desc", userOne, carOne, now, 1));
        Advert advertTwo = adService.saveAdvert(new Advert("Chevrolet ad", "Chevrolet ad desc", userTwo, carTwo, Date.from(now.toInstant().minusSeconds(86401)), 1));
        Photo photo = new Photo("name", "data".getBytes(), advertOne);
        advertOne.addPhoto(photo);
        advertOne.setPublished(true);
        advertTwo.setPublished(true);
        adService.saveAdvert(advertOne);
        adService.saveAdvert(advertTwo);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("lastday")).thenReturn("true");
        StringWriter testOut = new StringWriter();
        PrintWriter writer = new PrintWriter(testOut);
        Mockito.when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        ObjectMapper mapper = new ObjectMapper();
        String expected = new StringBuilder()
                .append("[{")
                .append("\"id\":").append(advertOne.getId()).append(",")
                .append("\"summary\":\"").append(advertOne.getSummary()).append("\",")
                .append("\"description\":\"").append(advertOne.getDescription()).append("\",")
                .append("\"closed\":").append(advertOne.isClosed()).append(",")
                .append("\"car\":").append("{")
                .append("\"id\":").append(advertOne.getCar().getId()).append(",")
                .append("\"brand\":\"").append(advertOne.getCar().getBrand()).append("\",")
                .append("\"model\":\"").append(advertOne.getCar().getModel()).append("\",")
                .append("\"body\":\"").append(advertOne.getCar().getBody()).append("\",")
                .append("\"produced\":").append(advertOne.getCar().getProduced().getTime()).append("},")
                .append("\"createdOn\":").append(advertOne.getCreatedOn().getTime()).append(",")
                .append("\"closedOn\":").append(advertOne.getClosedOn().getTime()).append(",")
                .append("\"published\":").append(advertOne.isPublished()).append(",")
                .append("\"price\":").append(advertOne.getPrice()).append(",")
                .append("\"photos\":[{")
                .append("\"id\":").append(advertOne.getPhotos().get(0).getId()).append(",")
                .append("\"name\":\"").append(advertOne.getPhotos().get(0).getName()).append("\",")
                .append("\"data\":").append(mapper.writeValueAsString(advertOne.getPhotos().get(0).getData()))
                .append("}]")
                .append("}]")
                .toString();
        new SearchServlet().doGet(req, resp);
        String result = testOut.toString();
        Assert.assertThat(result, is(expected));
        adService.deleteAdvert(advertOne);
        adService.deleteAdvert(advertTwo);
        carService.removeCar(carOne);
        carService.removeCar(carTwo);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }

    /**
     * Проверка получения данных об объявлениях только с фото.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws IOException                    исключения ввода и вывода.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenSearchWithPhotoThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException, IOException, ServletException {
        User userOne = userService.createUser("some1@email", "somepass1", "somename1");
        User userTwo = userService.createUser("some2@email", "somepass2", "somename3");
        Car carOne = carService.saveCar(new Car("Dodge", "Chalenger", "Sedan", new Date(1L)));
        Car carTwo = carService.saveCar(new Car("Chevrolet", "Lacetti", "Sedan", new Date(1L)));
        Advert advertOne = adService.saveAdvert(new Advert("Dodge ad", "Dodge ad desc", userOne, carOne, now, 1));
        Advert advertTwo = adService.saveAdvert(new Advert("Chevrolet ad", "Chevrolet ad desc", userTwo, carTwo, Date.from(now.toInstant().minusSeconds(86401)), 1));
        Photo photo = new Photo("name", "data".getBytes(), advertOne);
        advertOne.addPhoto(photo);
        advertOne.setPublished(true);
        advertTwo.setPublished(true);
        adService.saveAdvert(advertOne);
        adService.saveAdvert(advertTwo);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("onlyphoto")).thenReturn("true");
        StringWriter testOut = new StringWriter();
        PrintWriter writer = new PrintWriter(testOut);
        Mockito.when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        ObjectMapper mapper = new ObjectMapper();
        String expected = new StringBuilder()
                .append("[{")
                .append("\"id\":").append(advertOne.getId()).append(",")
                .append("\"summary\":\"").append(advertOne.getSummary()).append("\",")
                .append("\"description\":\"").append(advertOne.getDescription()).append("\",")
                .append("\"closed\":").append(advertOne.isClosed()).append(",")
                .append("\"car\":").append("{")
                .append("\"id\":").append(advertOne.getCar().getId()).append(",")
                .append("\"brand\":\"").append(advertOne.getCar().getBrand()).append("\",")
                .append("\"model\":\"").append(advertOne.getCar().getModel()).append("\",")
                .append("\"body\":\"").append(advertOne.getCar().getBody()).append("\",")
                .append("\"produced\":").append(advertOne.getCar().getProduced().getTime()).append("},")
                .append("\"createdOn\":").append(advertOne.getCreatedOn().getTime()).append(",")
                .append("\"closedOn\":").append(advertOne.getClosedOn().getTime()).append(",")
                .append("\"published\":").append(advertOne.isPublished()).append(",")
                .append("\"price\":").append(advertOne.getPrice()).append(",")
                .append("\"photos\":[{")
                .append("\"id\":").append(advertOne.getPhotos().get(0).getId()).append(",")
                .append("\"name\":\"").append(advertOne.getPhotos().get(0).getName()).append("\",")
                .append("\"data\":").append(mapper.writeValueAsString(advertOne.getPhotos().get(0).getData()))
                .append("}]")
                .append("}]")
                .toString();
        new SearchServlet().doGet(req, resp);
        String result = testOut.toString();
        Assert.assertThat(result, is(expected));
        adService.deleteAdvert(advertOne);
        adService.deleteAdvert(advertTwo);
        carService.removeCar(carOne);
        carService.removeCar(carTwo);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }

    /**
     * Проверка получения данных об объявлениях заданной марки.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws IOException                    исключения ввода и вывода.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenSearchByBrandThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException, IOException, ServletException {
        User userOne = userService.createUser("some1@email", "somepass1", "somename1");
        User userTwo = userService.createUser("some2@email", "somepass2", "somename3");
        Car carOne = carService.saveCar(new Car("Dodge", "Chalenger", "Sedan", new Date(1L)));
        Car carTwo = carService.saveCar(new Car("Chevrolet", "Lacetti", "Sedan", new Date(1L)));
        Advert advertOne = adService.saveAdvert(new Advert("Dodge ad", "Dodge ad desc", userOne, carOne, now, 1));
        Advert advertTwo = adService.saveAdvert(new Advert("Chevrolet ad", "Chevrolet ad desc", userTwo, carTwo, Date.from(now.toInstant().minusSeconds(86401)), 1));
        Photo photo = new Photo("name", "data".getBytes(), advertOne);
        advertOne.addPhoto(photo);
        advertOne.setPublished(true);
        advertTwo.setPublished(true);
        adService.saveAdvert(advertOne);
        adService.saveAdvert(advertTwo);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("bybrand")).thenReturn("Chevrolet");
        StringWriter testOut = new StringWriter();
        PrintWriter writer = new PrintWriter(testOut);
        Mockito.when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        String expected = new StringBuilder()
                .append("[{")
                .append("\"id\":").append(advertTwo.getId()).append(",")
                .append("\"summary\":\"").append(advertTwo.getSummary()).append("\",")
                .append("\"description\":\"").append(advertTwo.getDescription()).append("\",")
                .append("\"closed\":").append(advertTwo.isClosed()).append(",")
                .append("\"car\":").append("{")
                .append("\"id\":").append(advertTwo.getCar().getId()).append(",")
                .append("\"brand\":\"").append(advertTwo.getCar().getBrand()).append("\",")
                .append("\"model\":\"").append(advertTwo.getCar().getModel()).append("\",")
                .append("\"body\":\"").append(advertTwo.getCar().getBody()).append("\",")
                .append("\"produced\":").append(advertTwo.getCar().getProduced().getTime()).append("},")
                .append("\"createdOn\":").append(advertTwo.getCreatedOn().getTime()).append(",")
                .append("\"closedOn\":").append(advertTwo.getClosedOn().getTime()).append(",")
                .append("\"published\":").append(advertTwo.isPublished()).append(",")
                .append("\"price\":").append(advertTwo.getPrice()).append(",")
                .append("\"photos\":[]")
                .append("}]")
                .toString();
        new SearchServlet().doGet(req, resp);
        String result = testOut.toString();
        Assert.assertThat(result, is(expected));
        adService.deleteAdvert(advertOne);
        adService.deleteAdvert(advertTwo);
        carService.removeCar(carOne);
        carService.removeCar(carTwo);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }

    /**
     * Проверка получения данных об объявлениях конкретного пользователя.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws IOException                    исключения ввода и вывода.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenSearchByUserIdThenCorrect() throws UserAlreadyExistException, DuplicateAdvertForCarException, IOException, ServletException {
        User userOne = userService.createUser("some1@email", "somepass1", "somename1");
        User userTwo = userService.createUser("some2@email", "somepass2", "somename3");
        Car carOne = carService.saveCar(new Car("Dodge", "Chalenger", "Sedan", new Date(1L)));
        Car carTwo = carService.saveCar(new Car("Chevrolet", "Lacetti", "Sedan", new Date(1L)));
        Advert advertOne = adService.saveAdvert(new Advert("Dodge ad", "Dodge ad desc", userOne, carOne, now, 1));
        Advert advertTwo = adService.saveAdvert(new Advert("Chevrolet ad", "Chevrolet ad desc", userTwo, carTwo, Date.from(now.toInstant().minusSeconds(86401)), 1));
        Photo photo = new Photo("name", "data".getBytes(), advertOne);
        advertOne.addPhoto(photo);
        advertOne.setPublished(true);
        advertTwo.setPublished(true);
        adService.saveAdvert(advertOne);
        adService.saveAdvert(advertTwo);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("userId")).thenReturn(Integer.toString(userTwo.getId()));
        StringWriter testOut = new StringWriter();
        PrintWriter writer = new PrintWriter(testOut);
        Mockito.when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        String expected = new StringBuilder()
                .append("[{")
                .append("\"id\":").append(advertTwo.getId()).append(",")
                .append("\"summary\":\"").append(advertTwo.getSummary()).append("\",")
                .append("\"description\":\"").append(advertTwo.getDescription()).append("\",")
                .append("\"closed\":").append(advertTwo.isClosed()).append(",")
                .append("\"car\":").append("{")
                .append("\"id\":").append(advertTwo.getCar().getId()).append(",")
                .append("\"brand\":\"").append(advertTwo.getCar().getBrand()).append("\",")
                .append("\"model\":\"").append(advertTwo.getCar().getModel()).append("\",")
                .append("\"body\":\"").append(advertTwo.getCar().getBody()).append("\",")
                .append("\"produced\":").append(advertTwo.getCar().getProduced().getTime()).append("},")
                .append("\"createdOn\":").append(advertTwo.getCreatedOn().getTime()).append(",")
                .append("\"closedOn\":").append(advertTwo.getClosedOn().getTime()).append(",")
                .append("\"published\":").append(advertTwo.isPublished()).append(",")
                .append("\"price\":").append(advertTwo.getPrice()).append(",")
                .append("\"photos\":[]")
                .append("}]")
                .toString();
        new SearchServlet().doGet(req, resp);
        String result = testOut.toString();
        Assert.assertThat(result, is(expected));
        adService.deleteAdvert(advertOne);
        adService.deleteAdvert(advertTwo);
        carService.removeCar(carOne);
        carService.removeCar(carTwo);
        userService.deleteUser(userOne);
        userService.deleteUser(userTwo);
    }
}