package ru.job4j.cars.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.controller.TestFileUpload;
import ru.job4j.cars.controller.MockServletInputStream;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.Photo;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.*;
import ru.job4j.cars.service.exceptions.DuplicateAdvertForCarException;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Тесты класса PhotoServlet.
 */
public class PhotoServletTest {

    /**
     * Вспомогательные сервисы.
     */
    private final AdService adService = ServiceManager.getInstance().getAdService();
    private final UserService userService = ServiceManager.getInstance().getUserService();
    private final CarService carService = ServiceManager.getInstance().getCarService();
    private final PhotoService photoService = ServiceManager.getInstance().getPhotoService();

    /**
     * Проверка получения фото по идентикатору.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws IOException                    исключения ввода и вывода.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenGetPhotoByIdThenSuccess() throws UserAlreadyExistException, DuplicateAdvertForCarException, IOException, ServletException {
        User user = userService.createUser("testEmail", "testPass", "testName");
        Car car = carService.saveCar(new Car("testBrand", "testModel", "testBody", new Date(System.currentTimeMillis())));
        Advert advert = adService.saveAdvert(new Advert("testSummary", "testDescription", user, car, new Date(System.currentTimeMillis()), 1));
        Photo photo = new Photo("testName", "testData".getBytes(), advert);
        photoService.addPhoto(photo);
        advert = adService.getAdvertById(advert.getId());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ServletOutputStream servletOut = Mockito.mock(ServletOutputStream.class);
        Mockito.when(request.getParameter("photoId")).thenReturn(String.valueOf(photo.getId()));
        Mockito.when(response.getOutputStream()).thenReturn(servletOut);
        new PhotoServlet().doGet(request, response);
        Mockito.verify(response).setContentType("image/*");
        Mockito.verify(response).setContentType("name=" + photo.getName());
        Mockito.verify(response).setHeader("Content-Disposition", "attachment; filename=\"" + photo.getName() + "\"");
        Mockito.verify(servletOut).write("testData".getBytes());
        adService.deleteAdvert(advert);
        carService.removeCar(car);
        userService.deleteUser(user);
    }

    /**
     * Проверка получения фото для объявления по идентификатору объявления.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws IOException                    исключения ввода и вывода.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenGetPhotosByAdvertIdThenSuccess() throws UserAlreadyExistException, DuplicateAdvertForCarException, IOException, ServletException {
        User user = userService.createUser("testEmail", "testPass", "testName");
        Car car = carService.saveCar(new Car("testBrand", "testModel", "testBody", new Date(System.currentTimeMillis())));
        Advert advert = adService.saveAdvert(new Advert("testSummary", "testDescription", user, car, new Date(System.currentTimeMillis()), 1));
        Photo photo = new Photo("testName", "testData".getBytes(), advert);
        photoService.addPhoto(photo);
        advert = adService.getAdvertById(advert.getId());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Mockito.when(request.getParameter("adId")).thenReturn(String.valueOf(advert.getId()));
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(byteOut));
        List<Photo> photos = photoService.getPhotoByAdvertId(advert.getId());
        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(photos);
        new PhotoServlet().doGet(request, response);
        String result = byteOut.toString();
        Assert.assertThat(result, is(expected));
        adService.deleteAdvert(advert);
        carService.removeCar(car);
        userService.deleteUser(user);
    }

    /**
     * Проверка загрузки фото.
     *
     * @throws IOException                    исключения ввода и вывода.
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws DuplicateAdvertForCarException в случае, если объявление для автомобиля уже существует.
     * @throws ServletException               исключения при работе сервлета.
     */
    @Test
    public void whenUploadPhotoThenSuccess() throws IOException, UserAlreadyExistException, DuplicateAdvertForCarException, ServletException {
        User user = userService.createUser("testEmail", "testPass", "testName");
        Car car = carService.saveCar(new Car("testBrand", "testModel", "testBody", new Date(System.currentTimeMillis())));
        Advert advert = adService.saveAdvert(new Advert("testSummary", "testDescription", user, car, new Date(System.currentTimeMillis()), 1));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        TestFileUpload fileUpload = new TestFileUpload("test.jpg", "image/*", "testImageData".getBytes());
        builder = builder.addBinaryBody(fileUpload.getFilename(), fileUpload.getContents(), ContentType.create(fileUpload.getMimeType()), fileUpload.getFilename());
        builder = builder.addTextBody("adId", String.valueOf(advert.getId()));
        HttpEntity entity = builder.build();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        entity.writeTo(os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse responce = Mockito.mock(HttpServletResponse.class);
        Mockito.when(request.getMethod()).thenReturn("POST");
        Mockito.when(request.getContentType()).thenReturn(entity.getContentType());
        Mockito.when(request.getContentLength()).thenReturn((int) entity.getContentLength());
        Mockito.when(request.getInputStream()).thenReturn(new MockServletInputStream(is));
        new PhotoServlet().doPost(request, responce);
        advert = adService.getAdvertById(advert.getId());
        List<Photo> photos = photoService.getPhotoByAdvertId(advert.getId());
        Assert.assertThat(photos.size(), is(1));
        Assert.assertThat(photos.get(0).getData(), is("testImageData".getBytes()));
        Assert.assertThat(photos.get(0).getName(), is("test.jpg"));
        Assert.assertThat(advert.getPhotos().size(), is(1));
        Assert.assertThat(advert.getPhotos().get(0).getId(), is(photos.get(0).getId()));
        adService.deleteAdvert(advert);
        carService.removeCar(car);
        userService.deleteUser(user);
    }
}