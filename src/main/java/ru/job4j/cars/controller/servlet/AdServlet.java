package ru.job4j.cars.controller.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Car;
import ru.job4j.cars.persistence.entities.User;
import ru.job4j.cars.service.AdService;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.ServiceManager;
import ru.job4j.cars.service.exceptions.DuplicateAdvertForCarException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс сервлета, обрабатывающего запросы связанные с объявлением.
 */
public class AdServlet extends HttpServlet {

    /**
     * Используемые при работе сервисы.
     */
    private static final Logger LOGGER = LogManager.getLogger();
    private final CarService carService = ServiceManager.getInstance().getCarService();
    private final AdService adService = ServiceManager.getInstance().getAdService();

    /**
     * Перенаправление на страницу редактирования объявления.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException ошибки при работе сервлета.
     * @throws IOException      ошибки ввода и вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!(req.getParameter("adId") == null)) {
            int id = Integer.parseInt(req.getParameter("adId"));
            Advert advert = adService.getAdvertById(id);
            req.setAttribute("advert", advert);
            req.setAttribute("edit", "true");
        } else {
            User user = (User) req.getSession().getAttribute("user");
            Car emptyCar = carService.saveCar(new Car("", "", "", new Date(0L)));
            Advert emptyAdvert = null;
            try {
                emptyAdvert = adService.saveAdvert(new Advert("", "", user, emptyCar, new Date(System.currentTimeMillis()), 0));
            } catch (DuplicateAdvertForCarException e) {
                LOGGER.warn(e, e);
            }
            req.setAttribute("advert", emptyAdvert);
        }
        req.getRequestDispatcher("/WEB-INF/views/adcard.jsp").forward(req, resp);
    }

    /**
     * Сохранение данных об объявлении.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException ошибки при работе сервлета.
     * @throws IOException      ошибки ввода и вывода.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int adId = Integer.parseInt(req.getParameter("adId"));
        Advert advert = adService.getAdvertById(adId);
        Car car = advert.getCar();
        advert.setSummary(req.getParameter("summary"));
        advert.setDescription(req.getParameter("description"));
        if (req.getParameter("closed") != null && req.getParameter("closed").equals("on")) {
            advert.setClosed(true);
            advert.setClosedOn(new Date(System.currentTimeMillis()));
        } else {
            advert.setClosed(false);
            advert.setClosedOn(new Date(0L));
        }
        advert.setPrice(Integer.parseInt(req.getParameter("price")));
        if (req.getParameter("published") != null && req.getParameter("published").equals("on")) {
            advert.setPublished(true);
        }
        car.setBrand(req.getParameter("brand"));
        car.setModel(req.getParameter("model"));
        car.setBody(req.getParameter("body"));
        try {
            car.setProduced(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("produced")));
        } catch (ParseException e) {
            LOGGER.warn(e, e);
        }
        try {
            carService.saveCar(car);
            adService.saveAdvert(advert);
            resp.sendRedirect("board.do");
        } catch (DuplicateAdvertForCarException e) {
            req.setAttribute("error", "Advert for thar car already exists");
            req.getRequestDispatcher("/WEB-INF/views/adcard.jsp").forward(req, resp);
        }
    }
}