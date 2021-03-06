package ru.job4j.cars.controller.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.service.ServiceManager;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.service.exceptions.UserAlreadyExistException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс сервлета для регистрации пользователя.
 */
public class RegServlet extends HttpServlet {

    /**
     * Вспомогательные сервисы.
     */
    private static final Logger LOGGER = LogManager.getLogger();
    private final UserService service = ServiceManager.getInstance().getUserService();

    /**
     * Переход на страницу регистрации.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения в работе сервлета.
     * @throws IOException      исключения ввода/вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения в работе сервлета.
     * @throws IOException      исключения ввода и вывода.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                LOGGER.warn("New user trying to register with invalid credentials!");
                req.setAttribute("error", "Нужно заполнить все поля.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            } else {
                service.createUser(email, password, name);
                LOGGER.info("New user " + name + " with email " + email + " successfully registered.");
                req.setAttribute("info", "Регистрация прошла успешно. Используйте ранее введенные данные для входа.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } catch (UserAlreadyExistException e) {
            LOGGER.warn("New user trying to register with existing email!");
            req.setAttribute("error", "Пользователь с таким email уже существует!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}