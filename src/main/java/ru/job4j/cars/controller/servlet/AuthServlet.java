package ru.job4j.cars.controller.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.ServiceManager;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.service.exceptions.UnexistUserException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Класс сервлета для авторизации пользователей.
 */
public class AuthServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger();
    private final UserService service = ServiceManager.getInstance().getUserService();

    /**
     * Авторизация и перенаправление пользователей.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения в работе сервлета.
     * @throws IOException      ошибки ввода и вывода.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorMessage = "";
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = service.getUserByEmail(email);
            if (user.getPassword().equals(password)) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                LOGGER.info("User " + user.getName() + " logged into service.");
                resp.sendRedirect(req.getContextPath() + "/board.do");
            } else {
                LOGGER.info("User " + user.getName() + " trying to log in to service with invalid password.");
                errorMessage = "Неверный пароль!";
                req.setAttribute("error", errorMessage);
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } catch (UnexistUserException e) {
            LOGGER.warn("User trying to log in to service with invalid email " + email + ".");
            LOGGER.warn(e, e);
            errorMessage = "Пользователя с таким email не существует!";
            req.setAttribute("error", errorMessage);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    /**
     * Перенаправление на страницу авторизации.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения при работе сервлета.
     * @throws IOException      исключения ввода и вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}