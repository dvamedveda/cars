package ru.job4j.cars.controller.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.service.AdService;
import ru.job4j.cars.service.ServiceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Класс сервлета для выдачи объявлений по различным критериям.
 */
public class SearchServlet extends HttpServlet {

    /**
     * Вспомогательные сервисы.
     */
    private static final Logger LOGGER = LogManager.getLogger();
    private final AdService adService = ServiceManager.getInstance().getAdService();

    /**
     * Выдача объявлений в соответствии с переданным критерием.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения при работе сервлета.
     * @throws IOException      исключения ввода и вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ads = "";
        if (req.getParameter("lastday") != null && req.getParameter("lastday").equals("true")) {
            ads = AdService.asJson(adService.getLastDayAdverts());
            LOGGER.info("Requested list of last day ads from " + req.getRemoteAddr() + ":" + req.getRemotePort());
        } else if (req.getParameter("onlyphoto") != null && req.getParameter("onlyphoto").equals("true")) {
            ads = AdService.asJson(adService.getAdvertsWithPhoto());
            LOGGER.info("Requested list of only ads with photo from " + req.getRemoteAddr() + ":" + req.getRemotePort());
        } else if (req.getParameter("bybrand") != null) {
            ads = AdService.asJson(adService.getAdvertsByBrandName(req.getParameter("bybrand")));
            LOGGER.info("Requested list of ads by brand name " + req.getParameter("bybrand") + " from " + req.getRemoteAddr() + ":" + req.getRemotePort());
        } else if (req.getParameter("userId") != null) {
            ads = AdService.asJson(adService.getAdvertsByUserId(Integer.parseInt(req.getParameter("userId"))));
        } else {
            ads = AdService.asJson(adService.getAllAdverts());
            LOGGER.info("Requested list of all ads from " + req.getRemoteAddr() + ":" + req.getRemotePort());
        }
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.write(ads);
        } catch (Exception e) {
            LOGGER.warn(e, e);
        }
    }
}