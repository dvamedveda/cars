package ru.job4j.cars.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.cars.persistence.entities.Advert;
import ru.job4j.cars.persistence.entities.Photo;
import ru.job4j.cars.service.AdService;
import ru.job4j.cars.service.PhotoService;
import ru.job4j.cars.service.ServiceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Сервлет для обработки запросов, связанных с фотографиями.
 */
public class PhotoServlet extends HttpServlet {

    /**
     * Вспомогательные сервисы.
     */
    private static final Logger LOGGER = LogManager.getLogger();
    private final PhotoService photoService = ServiceManager.getInstance().getPhotoService();
    private final AdService adService = ServiceManager.getInstance().getAdService();

    /**
     * Сохранение фотографий для объявления.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения при работе сервлета.
     * @throws IOException      исключения ввода и вывода.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            int adId = 0;
            for (FileItem item : items) {
                if (item.isFormField() && item.getFieldName().equals("adId")) {
                    adId = Integer.parseInt(item.getString());
                    break;
                }
            }
            Advert advert = adService.getAdvertById(adId);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String name = item.getName();
                    byte[] data = item.get();
                    photoService.addPhoto(new Photo(name, data, advert));
                    LOGGER.info("Saved photo for advert with id: " + advert.getId());
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }

    /**
     * Выдача фотографий для объявления.
     *
     * @param req  объект запроса.
     * @param resp объект ответа.
     * @throws ServletException исключения при работе сервлета.
     * @throws IOException      исключения ввода и вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("photoId") != null) {
            int photoId = Integer.parseInt(req.getParameter("photoId"));
            Photo photo = photoService.getPhotoById(photoId);
            resp.setContentType("image/*");
            resp.setContentType("name=" + photo.getName());
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + photo.getName() + "\"");
            try (OutputStream out = resp.getOutputStream()) {
                byte[] image = photo.getData();
                out.write(image);
            }
        } else if (req.getParameter("adId") != null) {
            int adId = Integer.parseInt(req.getParameter("adId"));
            List<Photo> photos = photoService.getPhotoByAdvertId(adId);
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(photos);
            try (PrintWriter out = resp.getWriter()) {
                out.write(result);
            }
        }
    }
}