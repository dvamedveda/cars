package ru.job4j.cars.controller.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.controller.Answers;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.AdService;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.ServiceManager;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.service.exceptions.DuplicateAdvertForCarException;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;

/**
 * Тесты сервлета AdServlet.
 */
public class AdServletTest {

    /**
     * Вспомогательные сервисы.
     */
    private final UserService userService = ServiceManager.getInstance().getUserService();
    private final AdService adService = ServiceManager.getInstance().getAdService();
    private final CarService carService = ServiceManager.getInstance().getCarService();

    /**
     * Проверка, что при создании нового объявления создается объявление пустышка.
     *
     * @throws UserAlreadyExistException в случае, если пользователя уже существует.
     * @throws ServletException          исключения в работе сервлета.
     * @throws IOException               исключения ввода и вывода.
     */
    @Test
    public void whenEditNewThenCreatesTemplate() throws UserAlreadyExistException, ServletException, IOException {
        User newUser = userService.createUser("some@email", "somepass", "somename");
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Answers answers = new Answers();
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(session.getAttribute("user")).thenReturn(newUser);
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        Mockito.when(req.getSession()).thenReturn(session);
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/adcard.jsp")).thenReturn(requestDispatcher);
        new AdServlet().doGet(req, resp);
        Mockito.verify(requestDispatcher).forward(req, resp);
        Advert attachedAdvert = (Advert) req.getAttribute("advert");
        Advert persistedAdvert = adService.getAdvertById(attachedAdvert.getId());
        Car car = persistedAdvert.getCar();
        Assert.assertThat(attachedAdvert, is(persistedAdvert));
        adService.deleteAdvert(persistedAdvert);
        carService.removeCar(car);
        userService.deleteUser(newUser);
    }

    /**
     * Проверка корректного перехода к редактированию для существующего объявления.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws ServletException               исключения при работе сервлета.
     * @throws IOException                    исключения ввода и вывода.
     * @throws DuplicateAdvertForCarException в случае, если для автомобиля уже есть объявление.
     */
    @Test
    public void whenEditExistingThenOpensCorrect() throws UserAlreadyExistException, ServletException, IOException, DuplicateAdvertForCarException {
        User newUser = userService.createUser("some1@email", "somepass1", "somename1");
        Car car = carService.saveCar(new Car("Toyota", "Vitz", "Hatchback", new Date(100L)));
        Advert advert = adService.saveAdvert(new Advert("Toyota ad", "Toyota ad desc", newUser, car, new Date(1000L), 1));
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Answers answers = new Answers();
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(session.getAttribute("user")).thenReturn(newUser);
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        Mockito.when(req.getSession()).thenReturn(session);
        Mockito.when(req.getParameter("adId")).thenReturn(String.valueOf(advert.getId()));
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/adcard.jsp")).thenReturn(requestDispatcher);
        new AdServlet().doGet(req, resp);
        Mockito.verify(requestDispatcher).forward(req, resp);
        Advert attachedAdvert = (Advert) req.getAttribute("advert");
        Advert persistedAdvert = adService.getAdvertById(attachedAdvert.getId());
        Assert.assertThat(attachedAdvert, is(persistedAdvert));
        Assert.assertThat(req.getAttribute("edit"), is("true"));
        adService.deleteAdvert(persistedAdvert);
        carService.removeCar(car);
        userService.deleteUser(newUser);
    }

    /**
     * Проверка корректного сохранения данных при редактировании объявления.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws ServletException               исключения при работе сервлета.
     * @throws IOException                    исключения ввода и вывода.
     * @throws DuplicateAdvertForCarException в случае, если для автомобиля уже есть объявление.
     * @throws ParseException                 исключения парсинга данных.
     */
    @Test
    public void whenPostAdThenPersistsCorrect() throws UserAlreadyExistException, ServletException, IOException, DuplicateAdvertForCarException, ParseException {
        User newUser = userService.createUser("some1@email", "somepass1", "somename1");
        Car car = carService.saveCar(new Car("", "", "", new Date(0L)));
        Advert advert = adService.saveAdvert(new Advert("", "", newUser, car, new Date(System.currentTimeMillis()), 0));
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        Mockito.when(req.getParameter("adId")).thenReturn(String.valueOf(advert.getId()));
        Mockito.when(req.getParameter("summary")).thenReturn("Summary from servlet");
        Mockito.when(req.getParameter("description")).thenReturn("Description from servlet");
        Mockito.when(req.getParameter("price")).thenReturn("111");
        Mockito.when(req.getParameter("brand")).thenReturn("Brand from servlet");
        Mockito.when(req.getParameter("model")).thenReturn("Model from servlet");
        Mockito.when(req.getParameter("body")).thenReturn("Body from servlet");
        Mockito.when(req.getParameter("produced")).thenReturn("2000-01-01");
        new AdServlet().doPost(req, resp);
        Mockito.verify(resp).sendRedirect("board.do");
        Advert persistedAdvert = adService.getAdvertById(advert.getId());
        Assert.assertThat(persistedAdvert.getSummary(), is("Summary from servlet"));
        Assert.assertThat(persistedAdvert.getDescription(), is("Description from servlet"));
        Assert.assertThat(persistedAdvert.getPrice(), is(111));
        Assert.assertThat(persistedAdvert.isClosed(), is(false));
        Assert.assertThat(persistedAdvert.isPublished(), is(false));
        Assert.assertThat(persistedAdvert.getPrice(), is(111));
        Assert.assertThat(persistedAdvert.getCar().getBrand(), is("Brand from servlet"));
        Assert.assertThat(persistedAdvert.getCar().getModel(), is("Model from servlet"));
        Assert.assertThat(persistedAdvert.getCar().getBody(), is("Body from servlet"));
        Assert.assertThat(persistedAdvert.getCar().getProduced().getTime(), is(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01").getTime()));
        adService.deleteAdvert(advert);
        carService.removeCar(car);
        userService.deleteUser(newUser);
    }

    /**
     * Проверка сохранения с публикацией объявления.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws ServletException               исключения при работе сервлета.
     * @throws IOException                    исключения ввода вывода.
     * @throws DuplicateAdvertForCarException в случае, если для автомобиля уже есть объявление.
     * @throws ParseException                 исключения при парсинге данных.
     */
    @Test
    public void whenPublishAdThenPersistsCorrect() throws UserAlreadyExistException, ServletException, IOException, DuplicateAdvertForCarException, ParseException {
        User newUser = userService.createUser("some1@email", "somepass1", "somename1");
        Car car = carService.saveCar(new Car("", "", "", new Date(0L)));
        Advert advert = adService.saveAdvert(new Advert("", "", newUser, car, new Date(System.currentTimeMillis()), 0));
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        Mockito.when(req.getParameter("adId")).thenReturn(String.valueOf(advert.getId()));
        Mockito.when(req.getParameter("summary")).thenReturn("Summary from servlet published");
        Mockito.when(req.getParameter("description")).thenReturn("Description from servlet published");
        Mockito.when(req.getParameter("published")).thenReturn("on");
        Mockito.when(req.getParameter("price")).thenReturn("111");
        Mockito.when(req.getParameter("brand")).thenReturn("Brand from servlet published");
        Mockito.when(req.getParameter("model")).thenReturn("Model from servlet published");
        Mockito.when(req.getParameter("body")).thenReturn("Body from servlet published");
        Mockito.when(req.getParameter("produced")).thenReturn("2000-01-01");
        new AdServlet().doPost(req, resp);
        Mockito.verify(resp).sendRedirect("board.do");
        Advert persistedAdvert = adService.getAdvertById(advert.getId());
        Assert.assertThat(persistedAdvert.getSummary(), is("Summary from servlet published"));
        Assert.assertThat(persistedAdvert.getDescription(), is("Description from servlet published"));
        Assert.assertThat(persistedAdvert.getPrice(), is(111));
        Assert.assertThat(persistedAdvert.isClosed(), is(false));
        Assert.assertThat(persistedAdvert.isPublished(), is(true));
        Assert.assertThat(persistedAdvert.getPrice(), is(111));
        Assert.assertThat(persistedAdvert.getCar().getBrand(), is("Brand from servlet published"));
        Assert.assertThat(persistedAdvert.getCar().getModel(), is("Model from servlet published"));
        Assert.assertThat(persistedAdvert.getCar().getBody(), is("Body from servlet published"));
        Assert.assertThat(persistedAdvert.getCar().getProduced().getTime(), is(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01").getTime()));
        adService.deleteAdvert(advert);
        carService.removeCar(car);
        userService.deleteUser(newUser);
    }

    /**
     * Проверка сохранения с закрытием объявления.
     *
     * @throws UserAlreadyExistException      в случае, если пользователь уже существует.
     * @throws ServletException               исключения при работе сервлета.
     * @throws IOException                    исключения ввода вывода.
     * @throws DuplicateAdvertForCarException в случае, если для автомобиля уже есть объявление.
     * @throws ParseException                 исключения при парсинге данных.
     */
    @Test
    public void whenCloseAdThenPersistsCorrect() throws UserAlreadyExistException, ServletException, IOException, DuplicateAdvertForCarException, ParseException {
        User newUser = userService.createUser("some1@email", "somepass1", "somename1");
        Car car = carService.saveCar(new Car("", "", "", new Date(0L)));
        Advert advert = adService.saveAdvert(new Advert("", "", newUser, car, new Date(System.currentTimeMillis()), 0));
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        Mockito.when(req.getParameter("adId")).thenReturn(String.valueOf(advert.getId()));
        Mockito.when(req.getParameter("summary")).thenReturn("Summary from servlet published");
        Mockito.when(req.getParameter("description")).thenReturn("Description from servlet published");
        Mockito.when(req.getParameter("closed")).thenReturn("on");
        Mockito.when(req.getParameter("price")).thenReturn("111");
        Mockito.when(req.getParameter("brand")).thenReturn("Brand from servlet published");
        Mockito.when(req.getParameter("model")).thenReturn("Model from servlet published");
        Mockito.when(req.getParameter("body")).thenReturn("Body from servlet published");
        Mockito.when(req.getParameter("produced")).thenReturn("2000-01-01");
        new AdServlet().doPost(req, resp);
        Mockito.verify(resp).sendRedirect("board.do");
        Advert persistedAdvert = adService.getAdvertById(advert.getId());
        Assert.assertThat(persistedAdvert.getSummary(), is("Summary from servlet published"));
        Assert.assertThat(persistedAdvert.getDescription(), is("Description from servlet published"));
        Assert.assertThat(persistedAdvert.getPrice(), is(111));
        Assert.assertThat(persistedAdvert.isClosed(), is(true));
        Assert.assertThat(persistedAdvert.isPublished(), is(false));
        Assert.assertThat(persistedAdvert.getPrice(), is(111));
        Assert.assertThat(persistedAdvert.getCar().getBrand(), is("Brand from servlet published"));
        Assert.assertThat(persistedAdvert.getCar().getModel(), is("Model from servlet published"));
        Assert.assertThat(persistedAdvert.getCar().getBody(), is("Body from servlet published"));
        Assert.assertThat(persistedAdvert.getCar().getProduced().getTime(), is(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01").getTime()));
        adService.deleteAdvert(advert);
        carService.removeCar(car);
        userService.deleteUser(newUser);
    }
}