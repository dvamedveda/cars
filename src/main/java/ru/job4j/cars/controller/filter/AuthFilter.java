package ru.job4j.cars.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация фильтра для перенаправления неаутентифицированных пользователей
 * на страницу аутентификации.
 */
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if (isUriAllowed(uri)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (request.getSession().getAttribute("user") == null) {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    /**
     * Проверяем, находится ли URI в списке доступных без аутентификации.
     *
     * @param uri обрабатывающийся в данный момент адрес.
     * @return результат проверки.
     */
    private boolean isUriAllowed(String uri) {
        List<String> allowedUris = new ArrayList<>();
        allowedUris.add("register.do");
        allowedUris.add("auth.do");
        allowedUris.add("board.do");
        allowedUris.add("search.do");
        allowedUris.add("photo.do");
        boolean result = false;
        for (String allowedUri : allowedUris) {
            if (uri.endsWith(allowedUri)) {
                result = true;
                break;
            }
        }
        return result;
    }
}