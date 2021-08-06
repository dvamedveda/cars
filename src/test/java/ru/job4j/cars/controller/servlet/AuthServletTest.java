package ru.job4j.cars.controller.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.controller.Answers;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.ServiceManager;
import ru.job4j.cars.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Тесты класса AuthServlet.
 */
public class AuthServletTest {

    /**
     * Проверка перенаправления на главную страницу при успешной аутентификации.
     *
     * @throws IOException      исключения ввода вывода.
     * @throws ServletException исключения при работе сервлета.
     */
    @Test
    public void whenPostValidDataThenIndexPage() throws IOException, ServletException, UserAlreadyExistException {
        UserService userService = ServiceManager.getInstance().getUserService();
        User newUser = userService.createUser("auth@email", "authpass", "name");
        Answers answers = new Answers();
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.doAnswer(answers.new SetAnswer()).when(session).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(session).getAttribute(Mockito.anyString());
        Mockito.when(req.getParameter("email")).thenReturn("auth@email");
        Mockito.when(req.getParameter("password")).thenReturn("authpass");
        Mockito.when(req.getSession()).thenReturn(session);
        new AuthServlet().doPost(req, resp);
        Mockito.verify(resp).sendRedirect(req.getContextPath() + "/board.do");
        User resultUser = (User) session.getAttribute("user");
        Assert.assertEquals(resultUser, newUser);
        userService.deleteUser(newUser);
    }

    /**
     * Проверка аутентификации при неправильном пароле.
     *
     * @throws IOException      исключения ввода вывода.
     * @throws ServletException исключения при работе сервлета.
     */
    @Test
    public void whenPostInvalidPassThenLoginPage() throws IOException, ServletException, UserAlreadyExistException {
        UserService userService = ServiceManager.getInstance().getUserService();
        User newUser = userService.createUser("auth@email", "authpass", "name");
        Answers answers = new Answers();
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.doAnswer(answers.new SetAnswer()).when(session).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(session).getAttribute(Mockito.anyString());
        Mockito.when(req.getParameter("email")).thenReturn("auth@email");
        Mockito.when(req.getParameter("password")).thenReturn("1");
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(requestDispatcher);
        Mockito.when(req.getSession()).thenReturn(session);
        new AuthServlet().doPost(req, resp);
        Mockito.verify(requestDispatcher).forward(req, resp);
        User resultUser = (User) session.getAttribute("user");
        Assert.assertNull(resultUser);
        userService.deleteUser(newUser);
    }

    /**
     * Проверка аутентификации при несуществующей почте.
     *
     * @throws IOException      исключения ввода вывода.
     * @throws ServletException исключения при работе сервлета.
     */
    @Test
    public void whenPostInvalidEmailThenLoginPage() throws IOException, ServletException, UserAlreadyExistException {
        UserService userService = ServiceManager.getInstance().getUserService();
        User newUser = userService.createUser("auth@email", "authpass", "name");
        Answers answers = new Answers();
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.doAnswer(answers.new SetAnswer()).when(session).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(session).getAttribute(Mockito.anyString());
        Mockito.when(req.getParameter("email")).thenReturn("invalid@email");
        Mockito.when(req.getParameter("password")).thenReturn("1");
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(requestDispatcher);
        Mockito.when(req.getSession()).thenReturn(session);
        new AuthServlet().doPost(req, resp);
        Mockito.verify(requestDispatcher).forward(req, resp);
        User resultUser = (User) session.getAttribute("user");
        Assert.assertNull(resultUser);
        userService.deleteUser(newUser);
    }

    /**
     * Проверка перенаправления на страницу аутентификации.
     *
     * @throws IOException      исключения ввода вывода.
     * @throws ServletException исключения при работе сервлета.
     */
    @Test
    public void whenGetThenLoginPage() throws IOException, ServletException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(requestDispatcher);
        new AuthServlet().doGet(req, resp);
        Mockito.verify(requestDispatcher).forward(req, resp);
    }
}