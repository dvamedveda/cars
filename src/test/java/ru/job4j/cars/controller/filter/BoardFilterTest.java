package ru.job4j.cars.controller.filter;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс содержит тесты класса BoardFilter.
 */
public class BoardFilterTest {

    /**
     * Проверка, что при обращении к корневой странице приложения,
     * клиент перенаправляется на страницу с объявлениями.
     *
     * @throws IOException      исключения ввода и вывода.
     * @throws ServletException исключения в работе сервлета.
     */
    @Test
    public void whenNoRegisteredThenForwardToLogin() throws IOException, ServletException {
        String fakeContextPath = "myservice";
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        Mockito.when(req.getRequestURI()).thenReturn(fakeContextPath + "/");
        Mockito.when(req.getContextPath()).thenReturn(fakeContextPath);
        new BoardFilter().doFilter(req, resp, filterChain);
        Mockito.verify(resp).sendRedirect(req.getContextPath() + "/board.do");
    }
}