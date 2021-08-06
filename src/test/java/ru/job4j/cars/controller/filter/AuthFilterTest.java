package ru.job4j.cars.controller.filter;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.controller.Answers;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Класс содержит тесты класса AuthFilter.
 */
public class AuthFilterTest {

    /**
     * Проверка, что неаутентифицированный пользователь перенаправляется на страницу логина,
     * при доступе к странице, требущей аутентификации.
     *
     * @throws IOException      исключения ввода и вывода.
     * @throws ServletException исключения при работе сервлета.
     */
    @Test
    public void whenNoRegisteredThenForwardToLogin() throws IOException, ServletException {
        Answers answers = new Answers();
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.doAnswer(answers.new SetAnswer()).when(session).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(session).getAttribute(Mockito.anyString());
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(requestDispatcher);
        Mockito.when(req.getRequestURI()).thenReturn("adcard.do");
        Mockito.when(req.getSession()).thenReturn(session);
        new AuthFilter().doFilter(req, resp, filterChain);
        Mockito.verify(requestDispatcher).forward(req, resp);
    }
}