package ru.job4j.cars.controller.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.ServiceManager;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.service.exceptions.UnexistUserException;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Тесты класса LogoutServlet.
 */
public class LogoutServletTest {

    /**
     * Проверка выхода пользователя из приложения.
     *
     * @throws IOException               исключения ввода и вывода.
     * @throws ServletException          исключения при работе сервлета.
     * @throws UserAlreadyExistException в случае, если пользователь уже существует.
     * @throws UnexistUserException      в случае, если пользователя не существует.
     */
    @Test
    public void whenLogoutThenGoingBoard() throws IOException, ServletException, UserAlreadyExistException, UnexistUserException {
        UserService userService = ServiceManager.getInstance().getUserService();
        String name = "somename", email = "some@email", password = "somepass";
        User user = userService.createUser(email, password, name);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(req.getSession()).thenReturn(session);
        Mockito.when(req.getContextPath()).thenReturn("");
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(requestDispatcher);
        new LogoutServlet().doGet(req, resp);
        Mockito.verify(resp).sendRedirect("/board.do");
        userService.deleteUser(user);
    }
}