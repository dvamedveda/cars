package ru.job4j.cars.service;

/**
 * Класс-менеджер сервисов для хранения инстансов классов сервисного слоя.
 */
public class ServiceManager {

    /**
     * Переменные, хранящие сервисы.
     */
    private final CarService carService;
    private final UserService userService;
    private final AdService adService;
    private final PhotoService photoService;

    /**
     * Инициализация сервисов.
     */
    private ServiceManager() {
        carService = new CarService();
        userService = new UserService();
        adService = new AdService();
        photoService = new PhotoService();
    }

    /**
     * Класс реализующий шаблон синглтон для менеджера сервисов.
     */
    private static final class Lazy {
        private static final ServiceManager INST = new ServiceManager();
    }

    /**
     * Метод для получения инстанса менеджера сервисов.
     *
     * @return объект менеджера сервисов.
     */
    public static ServiceManager getInstance() {
        return Lazy.INST;
    }


    /**
     * Получить объект сервиса для работы с автомобилями.
     *
     * @return объект сервиса автомобилей.
     */
    public CarService getCarService() {
        return this.carService;
    }

    /**
     * Получить объект сервиса для работы с пользователями.
     *
     * @return объект сервиса пользователей.
     */
    public UserService getUserService() {
        return this.userService;
    }

    /**
     * Получить объект сервиса для работы с объявлениями.
     *
     * @return объект сервиса объявлений.
     */
    public AdService getAdService() {
        return this.adService;
    }

    /**
     * Получить объект сервиса для работы с фотографиями.
     *
     * @return объект сервиса фотографий.
     */
    public PhotoService getPhotoService() {
        return this.photoService;
    }
}