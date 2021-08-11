package ru.job4j.cars.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Реализация фильтра для перенаправления неаутентифицированных пользователей
 * на страницу аутентификации.
 */
public class AuthFilter implements Filter {

    /**
     * Ресурсы, доступные без авторизации.
     */
    private static final Set<String> ALLOWED_URIS = new HashSet<>();

    static {
        ALLOWED_URIS.add("register.do");
        ALLOWED_URIS.add("auth.do");
        ALLOWED_URIS.add("board.do");
        ALLOWED_URIS.add("search.do");
        ALLOWED_URIS.add("photo.do");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String requestedResource = Arrays.stream(uri.split("/")).skip(uri.split("/").length - 1).findFirst().get();
        if (ALLOWED_URIS.contains(requestedResource)) {
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
}