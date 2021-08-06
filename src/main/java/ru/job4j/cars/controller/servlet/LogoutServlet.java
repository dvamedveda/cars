package ru.job4j.cars.controller.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.persistence.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Класс сервлета для выхода пользователей из приложения.
 */
public class LogoutServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Выход из приложения и сброс сессии.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения при работе сервлета.
     * @throws IOException      исключения ввода и вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        session.invalidate();
        LOGGER.info("User " + user.getName() + " logged out from service.");
        resp.sendRedirect(req.getContextPath() + "/board.do");
    }
}